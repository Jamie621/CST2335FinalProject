package algonquin.cst2335.ju000013.recipeapi;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class RecipeViewModel extends ViewModel {
    public MutableLiveData<ArrayList<RecipeSaved>> savedrecipes = new MutableLiveData<>();
}