package us.drullk.nagacourtyarddisplay;

public class ConnectomeHelper {
    static String getPaddedBitString(byte b) {
        return String.format("%1$4s", Integer.toBinaryString(b & 0xF)).replace(' ', '0');
    }
    // https://en.wikipedia.org/wiki/Figure_space
    private static final String WHITESPACE = " ";

    static String getStringFromFacings(byte directions) {
        switch (directions & 0b1111) {
            case 0b0010:
                return "╷ ";
            case 0b0001:
                return "╶─";
            case 0b1000:
                return "╵ ";
            case 0b0100:
                return "╴";
            case 0b1001:
                return "└─";
            case 0b1100:
                return "┘ ";
            case 0b0110:
                return "┐ ";
            case 0b0011:
                return "┌─";
            case 0b1101:
                return "┴─";
            case 0b1110:
                return "┤ ";
            case 0b0111:
                return "┬─";
            case 0b1011:
                return "├─";
            case 0b1010:
                return "│ ";
            case 0b0101:
                return "──";
            case 0b1111:
                return "┼─";
            default:
                return "• ";
        }
    }
}
