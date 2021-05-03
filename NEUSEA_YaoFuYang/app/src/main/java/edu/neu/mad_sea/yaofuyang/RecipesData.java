package edu.neu.mad_sea.yaofuyang;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class RecipesData extends SQLiteOpenHelper {
    private static final String DB_NAME = "recipes.db";
    private static final int DB_VERSION = 1;

    // Standard String values for tables names in database.
    public static String RECIPES_TABLE = "recipes";
    public static String NUTRITION_LT = "nutrition_lt";
    public static String INGREDIENTS_LT = "ingredients_lt";
    public static String RECIPE_INGREDIENTS_TABLE = "recipe_ingredients_jt";
    public static String STEPS_TABLE = "steps";
    public static String RECIPE_STEP_TABLE = "recipe_step";

    public static String BOOKMARKED_RECIPES = "bookmarked_recipes";
    public static String INGREDIENTS_LIST = "ingredients_list";
    public static String REVIEW_LIST = "reviews";

    // Recipes table attribute names
    // Recipes(_id,name,minutes,date,nutritionId,nSteps,description,nIngredients)
    public static String RECIPE_ID = "recipeId";
    public static String RECIPE_NAME = "name";
    public static String RECIPE_MINUTES = "minutes";
    public static String RECIPE_DATE = "date";
    public static String RECIPE_NUTRITION_ID = "nutritionId";
    public static String RECIPE_N_STEPS = "nSteps";
    public static String RECIPE_N_INGREDIENTS = "nIngredients";

    // Nutrition lookup table attribute names
    // Nutrition_lt(_id, calories, totalFat, sugar, sodium, protein, satFat, carbohydrates)
    // PVD stands for "Percent daily value" since different units are used.
    public static String NUTRITION_CALORIES = "calories";
    public static String NUTRITION_TOTAL_FAT_PVD = "totalFatPVD";
    public static String NUTRITION_SUGAR_PVD = "sugarPVD";
    public static String NUTRITION_SODIUM_PVD = "sodiumPVD";
    public static String NUTRITION_PROTEIN_PVD = "proteinPVD";
    public static String NUTRITION_SATURATED_FAT_PVD = "satFatPVD";
    public static String NUTRITION_CARBOHYDRATES_PVD = "carbohydratesPVD";

    // Ingredients lookup table attribute names
    // Ingredients_lt(_id, ingredient)
    public static String INGREDIENT_ID = "ingredientId";
    public static String INGREDIENT_NAME = "ingredient";

    // RecipeIngredient Junction Table attribute names
    // RecipeIngredient(_id, recipeId, ingredientId)

    // Steps lookup table attribute names
    // Steps(_id, step)
    public static String STEP_ID = "stepId";
    public static String STEP_NAME = "step";

    // RecipeStep junction table attribute names
    // RecipeStep(_id, recipeId, stepId)

    // reviews(_id, recipeId, rating, review)
    public static String RATING = "rating";
    public static String REVIEW = "review";

    // Wrap the constructor with values we know relevant to this dbΩ
    public RecipesData(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createRecipes = "CREATE TABLE IF NOT EXISTS " + RECIPES_TABLE + "(" +
                "_id INTEGER PRIMARY KEY, " + RECIPE_NAME + " TEXT, " +
                RECIPE_MINUTES + " INTEGER, " + RECIPE_DATE + " TEXT, " +
                RECIPE_NUTRITION_ID + " INTEGER, " + RECIPE_N_STEPS + " INTEGER, " +
                RECIPE_N_INGREDIENTS + " INTEGER);";

        String createNutritionLt = "CREATE TABLE IF NOT EXISTS " + NUTRITION_LT + "(" +
                "_id INTEGER PRIMARY KEY, " + NUTRITION_CALORIES + " INTEGER, " +
                NUTRITION_TOTAL_FAT_PVD + " REAL, " + NUTRITION_SUGAR_PVD + " REAL, " +
                NUTRITION_SODIUM_PVD + " REAL, " + NUTRITION_PROTEIN_PVD + " REAL, " +
                NUTRITION_SATURATED_FAT_PVD + " REAL, " + NUTRITION_CARBOHYDRATES_PVD + " REAL);";

        String createIngredients = "CREATE TABLE IF NOT EXISTS " + INGREDIENTS_LT + "(" +
                "_id INTEGER PRIMARY KEY, " + INGREDIENT_NAME + " TEXT);";

        String createRecipeIngredient = "CREATE TABLE IF NOT EXISTS " + RECIPE_INGREDIENTS_TABLE +
                "(" + "_id INTEGER PRIMARY KEY, " + RECIPE_ID + " INTEGER, " +
                INGREDIENT_ID + " INTEGER);";

        String createStepLt = "CREATE TABLE IF NOT EXISTS " + STEPS_TABLE + "(" +
                "_id INTEGER PRIMARY KEY, " + STEP_NAME + " TEXT);";

        String createRecipeStep = "CREATE TABLE IF NOT EXISTS " + RECIPE_STEP_TABLE + "(" +
                "_id INTEGER PRIMARY KEY, " + RECIPE_ID + " INTEGER, " +
                STEP_ID + " INTEGER);";

        String createBookmarkedRecipes = "CREATE TABLE IF NOT EXISTS " + BOOKMARKED_RECIPES + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + RECIPE_ID + " INTEGER, " +
                RECIPE_NAME + " TEXT);";

        String createIngredientsList = "CREATE TABLE IF NOT EXISTS " + INGREDIENTS_LIST + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + INGREDIENT_NAME + " INTEGER);";

        String createReviewsList = "CREATE TABLE IF NOT EXISTS " + REVIEW_LIST + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + RECIPE_ID + " INTEGER, " +
                RATING + " REAL, " + REVIEW + " TEXT);";


        // Execute queries to create the tables holding recipe data..
        db.execSQL(createRecipes);
        db.execSQL(createNutritionLt);
        db.execSQL(createIngredients);
        db.execSQL(createRecipeIngredient);
        db.execSQL(createStepLt);
        db.execSQL(createRecipeStep);

        // Create the personal list tables.
        db.execSQL(createBookmarkedRecipes);
        db.execSQL(createIngredientsList);
        db.execSQL(createReviewsList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop all existing tables.
        db.execSQL("DROP TABLE IF EXISTS " + RECIPES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NUTRITION_LT);
        db.execSQL("DROP TABLE IF EXISTS " + INGREDIENTS_LT);
        db.execSQL("DROP TABLE IF EXISTS " + RECIPE_INGREDIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + STEPS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RECIPE_STEP_TABLE);

        // Recreate database.
        onCreate(db);
    }
}
