package stanford.edu.livetrak2;

import java.util.ArrayList;

class ColumnData {
    public String label = "";
    public ArrayList<OptionData> options = new ArrayList();

    public ColumnData(String label) {
        this.label = label;
    }

    public void addOption(OptionData od) {
        this.options.add(od);
    }
}
