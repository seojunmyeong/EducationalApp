package tutorials.crunchify.educationalapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class AssetManager {
    private Dictionary<String, String> foodPaths;
    private Dictionary<String, List<String>> levelFood;
    private Context context;

    public AssetManager(Context context, String levelNum){
        this.context = context;
        foodPaths = new Hashtable<>();
        levelFood = new Hashtable<>();

        try {
            List<String> foodNames = new Vector<>();
            String[] localPaths = context.getAssets().list(levelNum);
            for (String localPath : localPaths) {
                String foodName = localPath
                        .split("-")[1]
                        .split("\\.")[0];
                foodNames.add(foodName);
                foodPaths.put(foodName, levelNum + "/" + localPath);
            }
            levelFood.put(levelNum, foodNames);
        } catch (IOException e) {
            Log.e("AssetManager", e.toString());
        }
    }

    public List<String> allFoodNames(String foodName) {
        return levelFood.get(foodName);
    }

    public Drawable imageForFood(String foodName) {
        try {
            InputStream stream = context.getAssets().open(foodPaths.get(foodName));
            return Drawable.createFromStream(stream, foodName);
        } catch (IOException e) {
            Log.e("AssetManager", e.toString());
        }
        return null;
    }
}
