package com.example.thememorandum.Mine.important_thing;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.loader.content.CursorLoader;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.example.thememorandum.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

public class ReminderFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {

    private TextView mEmptyView;
    private RecyclerView mRecyclerView;
    private ReminderAdapter mAdapter;
    private ReminderType mType;
    private MultiSelector mMultiSelector;
    private ModalMultiSelectorCallback mActionModeCallBack;
    private ActionMode mActionMode;

    private SwipeRefreshLayout mRefreshLayout;
    private List<ReminderItem> mReminderItems;
    private ReminderViewHolder.OnClickListener mOnItemClickListener;
    private ReminderViewHolder.OnLongClickListener mOnItemLongClickListener;

    public interface EditListener {
        void startEditActivity(RecyclerView.ViewHolder holder);
    }

    private EditListener mEditListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMultiSelector = new MultiSelector();

        mActionModeCallBack = new ModalMultiSelectorCallback(mMultiSelector) {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                super.onCreateActionMode(actionMode, menu);
                Activity activity = getActivity();
                if (activity == null) {
                    return false;
                }
                activity.getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_del_item) {
                    deleteDialog(actionMode).show();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                mMultiSelector.clearSelections();
                mMultiSelector.setSelectable(false);
            }
        };

        mEditListener = new EditListener() {
            @Override
            public void startEditActivity(RecyclerView.ViewHolder holder) {
                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }

                int position = holder.getAdapterPosition();
                ReminderItem item = mAdapter.getItemAtPosition(position);
                Intent intent=null;
                if (item.getType().equals(ReminderType.ALERT)) {
                    intent = new Intent(getContext(), createOrEditAlert.class);
                }

                intent.putExtra("data", item);
                activity.startActivity(intent);

            }
        };

        mOnItemClickListener = new ReminderViewHolder.OnClickListener() {
            @Override
            public void onClick(ReminderViewHolder holder) {
                if (mMultiSelector == null || mEditListener == null) {
                    return;
                }
                if (!mMultiSelector.tapSelection(holder)) {
                    mEditListener.startEditActivity(holder);
                } else if (mMultiSelector.getSelectedPositions().isEmpty() && mActionMode != null) {
                    mActionMode.finish();
                }
                mAdapter.notifyItemChanged(holder.getAdapterPosition());
            }
        };

        mOnItemLongClickListener = new ReminderViewHolder.OnLongClickListener() {
            @Override
            public boolean onLongClick(ReminderViewHolder holder) {
                if (getActivity() == null || mMultiSelector == null) {
                    return false;
                }

                if (!mMultiSelector.isSelectable()) {
                    mActionMode = ((AppCompatActivity) getActivity())
                            .startSupportActionMode(mActionModeCallBack);
                    mMultiSelector.setSelectable(true);
                    mMultiSelector.setSelected(holder, true);
                    mAdapter.notifyItemChanged(holder.getAdapterPosition());
                    return true;
                }
                return false;
            }
        };

        mType = getArguments() != null ? (ReminderType) getArguments()
                .getSerializable(ReminderParams.TYPE) : null;

        mReminderItems = new ArrayList<>();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.important_main_fragment, container, false);
        setupFAB(view);

        mRecyclerView = view.findViewById(R.id.reminder_list);
        mEmptyView = view.findViewById(R.id.empty);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    public void onResume() {
        super.onResume();
        mRefreshLayout.setRefreshing(true);
        restartLoader();
    }

    private void setupFAB(View view) {
        FloatingActionMenu floatingActionMenu = view.findViewById(R.id.floating_menu);
        floatingActionMenu.setClosedOnTouchOutside(true);
        FloatingActionButton addAlert = view.findViewById(R.id.add_alert);

        addAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), createOrEditAlert.class));
            }
        });

    }

    private AlertDialog deleteDialog(final ActionMode actionMode) {
        return new AlertDialog.Builder(getContext(),R.style.MyDialogStyle)
                .setIcon(R.drawable.shanchu)
                .setTitle("Tips")
                .setMessage("是否确定删除")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        deleteSelected();
                        dialog.dismiss();
                        actionMode.finish();
                        mMultiSelector.clearSelections();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                })
                .create();

    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void update() {
        if (mReminderItems == null || mReminderItems.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mRefreshLayout.setRefreshing(false);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new ReminderAdapter(mReminderItems, mOnItemClickListener,
                    mOnItemLongClickListener);
            mAdapter.setHasStableIds(true);
            mAdapter.setMultiSelector(mMultiSelector);
            mRecyclerView.swapAdapter(mAdapter, false);
        }
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * Loader callbacks
     */

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (getContext() == null) {
            return null;
        }

        switch(mType){
            case ALL:
                return new CursorLoader(getContext(), ReminderContract.All.CONTENT_URI, null, null,
                        null, null);
            case ALERT:
                return new CursorLoader(getContext(), ReminderContract.Alerts.CONTENT_URI, null, null,
                        null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mReminderItems.clear();
        for (boolean i = data.moveToFirst(); i; i = data.moveToNext()) {
            ReminderItem item = new ReminderItem(data);
            mReminderItems.add(item);
        }
        update();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(0, null, this);
    }

    /**
     * SwipeRefreshLayout callback
     */

    @Override
    public void onRefresh() {
        restartLoader();
    }

    private void deleteSelected() {
        Context context = getContext();
        if (context == null) {
            return;
        }

        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            return;
        }

        Uri uri;
        List<Integer> positions = mMultiSelector.getSelectedPositions();
        for (int j = 0; j < positions.size(); j++) {
            int position = positions.get(j);
            ReminderItem item = mAdapter.getItemAtPosition(position);
            int id = item.getId();
            ReminderType type = item.getType();

            if (type.equals(ReminderType.ALERT)) {
                Intent delete = new Intent(getContext(), AlarmService02.class);
                delete.putExtra(ReminderParams.ID, id);
                delete.setAction(AlarmService02.DELETE);
                getContext().startService(delete);
            } else {
                uri = ContentUris.withAppendedId(ReminderContract.Alerts.CONTENT_URI, id);
                getContext().getContentResolver().delete(uri, null, null);
            }
        }
    }
}