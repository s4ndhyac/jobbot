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

        String range = "C2:H183";
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = response.getValues();

        for (List row : values) {
            if(row.get(2).toString() == "1")
            {
                System.out.printf("%s, %s\n", row.get(0), row.get(4));
                h.put("company", row.get(0).toString());
                h.put("email", row.get(1).toString());
                h.put("mission", row.get(5).toString());
            }
        }
        return h;
    }

}

