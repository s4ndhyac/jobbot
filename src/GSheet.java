import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GSheet {
    private static Sheets sheetsService;
    private static String SPREADSHEET_ID = "1evyzZySyKK7xJZ__Hi57MMjq1CIb40lnskYO6WeIWxY";

    public static Map<String, String> readData() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
        Map<String, String> h = new HashMap<>();

        String range = "B2:F3";
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = response.getValues();

        for (List row : values) {
            // Print columns B and F, which correspond to indices 0 and 4.
            System.out.printf("%s, %s\n", row.get(0), row.get(4));
            h.put("company", row.get(0).toString());
            h.put("mission", row.get(4).toString());
        }
        return h;




    }

}

