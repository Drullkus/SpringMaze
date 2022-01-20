package us.drullk.nagacourtyarddisplay;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Random;

public class Connectome {
    @Setter
    @Getter
    @Min(2)
    @Max(30)
    private int width, height;
    @Setter
    @Getter
    private int seed;
    private transient byte[] mapping;

    public Connectome(/*int width, int height,*/ int seed) {
        //this.width = width;
        //this.height = height;
        this.seed = seed;

        //System.out.printf("Size %s %s with length %s%n", this.width, this.height, this.mapping.length);

        //this.generate();
    }

    public void generate() {
        this.mapping = new byte[this.width * this.height + 1 >> 1];
        new Random(this.seed).nextBytes(this.mapping);

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                for (Direction direction : Direction.values()) {
                    int xD = x + direction.X_OFFSET;
                    int yD = y + direction.Y_OFFSET;

                    if (xD >= 0
                            && xD < this.width
                            && yD >= 0
                            && yD < this.height) {
                        if (direction.testFor(this.getData(x, y))) {
                            // If there is connection in a direction, connect the adjacent piece
                            this.orData(xD, yD, direction.OPPOSITE);
                        } else {
                            // Otherwise completely sever it
                            this.andData(x, y, direction.INVERTED);
                            this.andData(xD, yD, direction.INVERTED_OPPOSITE);
                        }
                    }
                }
            }
        }
    }

    private void orData(int x, int y, byte data) {
        data &= 0b1111;
        data <<= ((x + y) & 1) << 2;

        this.mapping[this.getSingleCoordFromDouble(x, y)] |= data;
    }

    private void andData(int x, int y, byte data) {
        data &= 0b1111;
        data <<= ((x + y) & 1) << 2;
        data |= 0b1111 << (((x ^ 1) + y & 1) << 2);

        this.mapping[this.getSingleCoordFromDouble(x, y)] &= data;
    }

    private byte getData(int x, int y) {
        byte b = this.mapping[this.getSingleCoordFromDouble(x, y)];
        b >>= ((x + y) & 1) << 2;
        b &= 0b1111;

        return b;
    }

    private int getSingleCoordFromDouble(int x, int y) {
        return x + y * this.width >> 1;
    }

    @Override
    public String toString() {
        this.generate();

        StringBuilder maze = new StringBuilder();

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                maze.append(ConnectomeHelper.getStringFromFacings(this.getData(x, y)));
            }

            maze.append("<br>");
        }

        return maze.toString();
    }

    public enum Direction {
        EAST ((byte) 0b0001,  1,  0),
        SOUTH((byte) 0b0010,  0,  1),
        WEST ((byte) 0b0100, -1,  0),
        NORTH((byte) 0b1000,  0, -1);

        public final byte BYTE;
        public final byte OPPOSITE;
        public final byte INVERTED;
        public final byte INVERTED_OPPOSITE;
        public final int  X_OFFSET;
        public final int  Y_OFFSET;

        Direction(byte bite, int xOffset, int yOffset) {
            this.BYTE = bite;
            this.OPPOSITE = (byte) (bite >>> 2 | bite << 2);
            this.INVERTED = (byte) ~this.BYTE;
            this.INVERTED_OPPOSITE = (byte) ~this.OPPOSITE;
            this.X_OFFSET = xOffset;
            this.Y_OFFSET = yOffset;
        }

        private boolean testFor(byte directions) {
            return (this.BYTE & directions) == this.BYTE;
        }
    }
}

