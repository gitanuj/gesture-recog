import com.fastdtw.timeseries.TimeSeries;
import com.fastdtw.timeseries.TimeSeriesPoint;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.StringTokenizer;

public class Utils {

    public static void startThreadWithName(Runnable runnable, String name) {
        Thread thread = new Thread(runnable);
        thread.setName(name);
        thread.start();
    }

    public static String readFully(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(br);
        }
        return sb.toString();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static TimeSeries dataToTimeSeries(String data) {
        data = data.trim();
        int count = 0;
        TimeSeries timeSeries = new TimeSeries(6);
        StringTokenizer tokenizer = new StringTokenizer(data, " ,");
        while (tokenizer.hasMoreTokens()) {
            double[] values = new double[6];
            for (int i = 0; i < 6; ++i) {
                values[i] = Double.parseDouble(tokenizer.nextToken());
            }
            timeSeries.addLast(count, new TimeSeriesPoint(values));
            count++;
        }
        return timeSeries;
    }

    public static JsonObject mapToJsonObject(Map<String, String> map) {
        JsonObject jsonObject = new JsonObject();
        for (String key : map.keySet()) {
            jsonObject.addProperty(key, map.get(key));
        }
        return jsonObject;
    }

    public static Map<String, String> jsonStringToMap(String jsonString) {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return new Gson().fromJson(jsonString, type);
    }

    public static void writeMapToFile(File file, Map<String, String> map) {
        JsonObject jsonObject = mapToJsonObject(map);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file));
            writer.write(jsonObject.toString());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(writer);
        }
    }

    public static Map<String, String> readMapFromFile(File file) {
        try {
            if (file.exists()) {
                String jsonString = readFully(new FileInputStream(file));
                return jsonStringToMap(jsonString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
