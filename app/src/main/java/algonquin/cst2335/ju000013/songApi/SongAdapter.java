package algonquin.cst2335.ju000013.songApi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private List<Song> songsEntity;
    private Context context;

    public SongAdapter(List<Song> songsEntity, Context context) {
        this.songsEntity = songsEntity;
        this.context = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.song_info,null);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.SongViewHolder songViewHolder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return songsEntity == null ? 0: songsEntity.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        private TextView songName;
        private TextView songDuration;
        private TextView songAlbumName;
        private ImageView songAlbumCover;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.textViewSongTitle);
            songDuration = itemView.findViewById(R.id.textViewSongDuration);
            songAlbumName = itemView.findViewById(R.id.textViewSongAlbumName);
            songAlbumCover = itemView.findViewById(R.id.imageViewSongAlbumCover);
        }
    }
}
