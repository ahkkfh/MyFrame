package longimage.matisse;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import longimage.matisse.ui.MediaSelectionFragment;
import longimage.matisse.ui.adapter.AlbumMediaAdapter;
import longimage.matisse.ui.adapter.AlbumsAdapter;
import longimage.matisse.ui.widget.AlbumsSpinner;
import longimage.matisse.utils.MediaStoreCompat;

/***
 * @author marks.luo
 * @Description: (Main Activity to display albums and media content (images/videos) in each album
 * and also support media selecting operations.)
 * @date:2017-08-08 11:54
 *
 */
public class MatisseActivity extends AppCompatActivity implements
        AlbumCollection.AlbumCallbacks, AdapterView.OnItemSelectedListener,
        MediaSelectionFragment.SelectionProvider, View.OnClickListener,
        AlbumMediaAdapter.CheckStateListener, AlbumMediaAdapter.OnMediaClickListener,
        AlbumMediaAdapter.OnPhotoCapture {
    public static final String EXTRA_RESULT_SELECTION = "extra_result_selection";
    public static final String EXTRA_RESULT_SELECTION_PATH = "extra_result_selection_path";
    private static final int REQUEST_CODE_PREVIEW = 23;
    private static final int REQUEST_CODE_CAPTURE = 24;
    private final AlbumCollection mAlbumCollection = new AlbumCollection();
    private MediaStoreCompat mMediaStoreCompat;
    private SelectedItemCollection mSelectedCollection = new SelectedItemCollection(this);
    private SelectionSpec mSpec;

    private AlbumsSpinner mAlbumsSpinner;
    private AlbumsAdapter mAlbumsAdapter;
    private TextView mButtonPreview;
    private TextView mButtonApply;
    private View mContainer;
    private View mEmptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onAlbumLoad(Cursor cursor) {

    }

    @Override
    public void onAlbumReset() {

    }

    @Override
    public SelectedItemCollection provideSelectedItemCollection() {
        return null;
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMediaClick(Album album, Item item, int adapterPosition) {

    }

    @Override
    public void capture() {

    }
}
