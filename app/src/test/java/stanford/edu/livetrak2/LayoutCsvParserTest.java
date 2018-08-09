package stanford.edu.livetrak2;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import android.graphics.Color;


import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class LayoutCsvParserTest {

    @Test
    public void parser_isCorrect() throws IOException {
        LayoutCsvParser instance = new LayoutCsvParser();
        LayoutData layoutData = instance.parse(new FileInputStream("src/main/assets/LiveTrak_soil_v1.csv"));
        assertEquals(layoutData.columnDatas.size(), 12);
        for(ColumnData columnData : layoutData.columnDatas) {
            for(OptionData optionData : columnData.optionDatas) {
                assertEquals(3, optionData.color);
                assertNotNull(optionData.group);
                assertNotNull(optionData.logMsg);
                assertNotNull(optionData.text);
                assertNotEquals("", optionData.group);
                assertNotEquals("", optionData.logMsg);
                assertNotEquals("", optionData.text);
            }
        }
    }
}