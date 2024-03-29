package algonquin.cst2335.ju000013.songApi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import algonquin.cst2335.ju000013.R;
import algonquin.cst2335.ju000013.songApi.Song;

public class SongFavoritesAdapter extends RecyclerView.Adapter<SongFavoritesAdapter.SongFavoritesViewHolder> {
    private List<Song> songsEntity;
    private Context context;

    public SongFavoritesAdapter(List<Song> songsEntity, Context context) {
        this.songsEntity = songsEntity;
        this.context = context;
    }

    public void setSongs(List<Song> songs) {
        this.songsEntity = songs;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @NonNull
    @Override
    public SongFavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.song_info, null);
        return new SongFavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongFavoritesViewHolder songViewHolder, int position) {
        songViewHolder.songName.setText(songsEntity.get(position).getTitle());
        songViewHolder.songDuration.setText(String.valueOf(songsEntity.get(position).getDuration()));
        songViewHolder.songAlbumName.setText(songsEntity.get(position).getAlbumName());
        new Thread(() -> {
            try {
                URL imageUrl = new URL(songsEntity.get(position).getAlbumCoverUrl());
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Update ImageView on the main UI thread
                songViewHolder.itemView.post(() -> songViewHolder.songAlbumCover.setImageBitmap(bitmap));
            } catch (IOException e) {
                Log.e(null, "Error downloading image: " + e.getMessage());
            }
        }).start();

        songViewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SongDetailsActivity.class);
            intent.putExtra("SONG_TITLE", songsEntity.get(position).getTitle());
            intent.putExtra("SONG_DURATION", songsEntity.get(position).getDuration());
            intent.putExtra("SONG_ALBUM_NAME", songsEntity.get(position).getAlbumName());
            intent.putExtra("SONG_ALBUM_COVER_URL", songsEntity.get(position).getAlbumCoverUrl());
            Bundle extras = new Bundle();
            extras.putString("SOURCE", "FAVORITES");
            Intent prevIntent = ((SongFavoritesActivity) context).getIntent();
            Bundle prevExtras = prevIntent.getExtras();
            if (prevExtras != null) {
                extras.putAll(prevExtras);
            }
            intent.putExtras(extras);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return songsEntity == null ? 0 : songsEntity.size();
    }

    public class SongFavoritesViewHolder extends RecyclerView.ViewHolder {
        private TextView songName;
        private TextView songDuration;
        private TextView songAlbumName;
        private ImageView songAlbumCover;

        public SongFavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.textViewSongTitle);
            songDuration = itemView.findViewById(R.id.textViewSongDuration);
            songAlbumName = itemView.findViewById(R.id.textViewSongAlbumName);
            songAlbumCover = itemView.findViewById(R.id.imageViewSongAlbumCover);
        }
    }
}