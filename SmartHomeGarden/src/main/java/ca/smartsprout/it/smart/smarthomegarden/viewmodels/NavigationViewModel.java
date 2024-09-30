import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// NavigationViewModel.java
public class NavigationViewModel extends ViewModel {

    private final MutableLiveData<Integer> selectedItem = new MutableLiveData<>();

    public LiveData<Integer> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int itemId) {
        selectedItem.setValue(itemId);
    }
}
