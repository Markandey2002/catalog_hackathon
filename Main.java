import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static long decodeBaseValue(String value, int base) {
        return Long.parseLong(value, base);
    }

    private static long calculateConstantTerm(List<long[]> points, int k) {
        long constant = 0;

        for (int i = 0; i < k; i++) {
            long[] pointI = points.get(i);
            long xi = pointI[0], yi = pointI[1];

            double Li0 = 1.0;
            for (int j = 0; j < k; j++) {
                if (j != i) {
                    long xj = points.get(j)[0];
                    Li0 *= (0 - xj) / (double) (xi - xj);
                }
            }
            constant += yi * Li0;
        }
        return constant;
    }

    private static long extractConstantTerm(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject json = new JSONObject(content);
            int n = json.getJSONObject("keys").getInt("n");
            int k = json.getJSONObject("keys").getInt("k");

            List<long[]> points = new ArrayList<>();

            for (int i = 1; i <= n; i++) {
                String key = String.valueOf(i);
                if (json.has(key)) {
                    JSONObject jsonObj = json.getJSONObject(key);
                    String base = jsonObj.getString("base");
                    String value = jsonObj.getString("value");

                    long decodedValue = decodeBaseValue(value, Integer.parseInt(base));
                    points.add(new long[]{i, decodedValue});
                }
            }

            return calculateConstantTerm(points, k);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        long constantTerm1 = extractConstantTerm("input1.json");
        long constantTerm2 = extractConstantTerm("input2.json");

        System.out.println("Constant term (c) for input1.json: " + constantTerm1);
        System.out.println("Constant term (c) for input2.json: " + constantTerm2);
    }
}

