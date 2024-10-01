/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

package ca.smartsprout.it.smart.smarthomegarden.viewmodels;

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
