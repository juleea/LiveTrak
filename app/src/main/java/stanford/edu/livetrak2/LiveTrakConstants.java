package stanford.edu.livetrak2;

public interface LiveTrakConstants {
    public static final Language DEFAULT_LANGUAGE = Language.BANGLA;
    public static final int HEADING_SIZE = 17;

    public enum Language {
        ENGLISH(1),
        BANGLA(2);
        
        private final int indx;

        private Language(int indx) {
            this.indx = indx;
        }

        public int getIndex() {
            return this.indx;
        }
    }
}
