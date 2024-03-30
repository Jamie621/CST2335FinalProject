package algonquin.cst2335.ju000013.sunrisesunsetApi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import algonquin.cst2335.ju000013.R;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    private List<SaveLocation> favourites;
    private FavouriteViewModel favouriteViewModel;
    private RecyclerView recyclerView;
    private boolean isDeleteMode = false;

    public void removeItem(int position) {
        if (position < favourites.size()) {
            favourites.remove(position);
            notifyItemRemoved(position);
        }
    }


    public void setFavourites(List<SaveLocation> newFavourites) {
        this.favourites = newFavourites;
        notifyDataSetChanged();
    }

    // Interface for handling delete clicks
    public interface OnItemDeleteListener {
        void onItemDelete(SaveLocation location, int position);
    }

    private OnItemDeleteListener onItemDeleteListener;

    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.onItemDeleteListener = listener;
    }

    public void setDeleteMode(boolean isDeleteMode) {
        this.isDeleteMode = isDeleteMode;
    }

    public FavouriteAdapter(List<SaveLocation> favourites, FavouriteViewModel viewModel, RecyclerView recyclerView) {
        this.favourites = favourites;
        this.favouriteViewModel = viewModel;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        SaveLocation location = favourites.get(position);
        holder.tvDate.setText(location.getDate());
        holder.tvSunrise.setText(location.getSunrise());
        holder.tvSunset.setText(location.getSunset());
        holder.tvLatitude.setText(location.getLatitude());
        holder.tvLongitude.setText(location.getLongitude());
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvSunrise, tvSunset, tvLatitude, tvLongitude;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSunrise = itemView.findViewById(R.id.tvSunrise);
            tvSunset = itemView.findViewById(R.id.tvSunset);
            tvLatitude = itemView.findViewById(R.id.tvLatitude);
            tvLongitude = itemView.findViewById(R.id.tvLongitude);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    SaveLocation selectedLocation = favourites.get(position);
                    if (isDeleteMode && onItemDeleteListener != null) {
                        // Call the method from the interface to handle the delete
                        onItemDeleteListener.onItemDelete(selectedLocation, position);
                    } else {
                        // Not in delete mode, add a new location based on the current one
                        String newTime = getCurrentTime(); // Use the method to get the current time
                        SaveLocation newLocation = new SaveLocation(
                                selectedLocation.getSunrise(),
                                selectedLocation.getSunset(),
                                newTime, // new time
                                selectedLocation.getLocation(), // same location name
                                selectedLocation.getLatitude(),
                                selectedLocation.getLongitude()
                        );

                        // Insert the new location into the database and update RecyclerView
                        favouriteViewModel.insertFavourite(newLocation);
                        favourites.add(newLocation);
                        notifyItemInserted(favourites.size() - 1);

                        // Show confirmation message
                        Snackbar.make(recyclerView, "New location added!", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }

        // Helper method to get the current time
        private String getCurrentTime() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    }
}


