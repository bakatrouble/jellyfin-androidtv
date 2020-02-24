package org.jellyfin.androidtv.browsing;

import android.os.Bundle;

import org.jellyfin.androidtv.R;
import org.jellyfin.androidtv.TvApp;
import org.jellyfin.androidtv.querying.StdItemQuery;

import java.util.Arrays;

import org.jellyfin.androidtv.util.Utils;
import org.jellyfin.apiclient.model.dto.BaseItemType;
import org.jellyfin.apiclient.model.entities.SortOrder;
import org.jellyfin.apiclient.model.livetv.RecordingQuery;
import org.jellyfin.apiclient.model.querying.ItemFields;
import org.jellyfin.apiclient.model.querying.ItemFilter;
import org.jellyfin.apiclient.model.querying.ItemSortBy;

public class GenericFolderFragment extends EnhancedBrowseFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private static BaseItemType[] showSpecialViewTypes = new BaseItemType[] {BaseItemType.CollectionFolder, BaseItemType.Folder, BaseItemType.UserView, BaseItemType.ChannelFolderItem };

    @Override
    protected void setupQueries(IRowLoader rowLoader) {

        if (mFolder.getBaseItemType() == BaseItemType.RecordingGroup){
            RecordingQuery query = new RecordingQuery();
            query.setUserId(TvApp.getApplication().getCurrentUser().getId());
            query.setGroupId(mFolder.getId());
            query.setFields(new ItemFields[] {
                    ItemFields.PrimaryImageAspectRatio,
                    ItemFields.ChildCount
            });
            mRows.add(new BrowseRowDef(mApplication.getResources().getString(R.string.lbl_all_items), query));
            rowLoader.loadRows(mRows);
        } else {

            if (Utils.getSafeValue(mFolder.getChildCount(), 0) > 0 ||
                    mFolder.getBaseItemType() == BaseItemType.Channel ||
                    mFolder.getBaseItemType() == BaseItemType.ChannelFolderItem ||
                    mFolder.getBaseItemType() == BaseItemType.UserView ||
                    mFolder.getBaseItemType() == BaseItemType.CollectionFolder) {
                boolean showSpecialViews = Arrays.asList(showSpecialViewTypes).contains(mFolder.getBaseItemType()) && !"channels".equals(mFolder.getCollectionType());

                if (showSpecialViews) {
                    if (mFolder.getBaseItemType() != BaseItemType.ChannelFolderItem) {
                        StdItemQuery resume = new StdItemQuery();
                        resume.setParentId(mFolder.getId());
                        resume.setLimit(50);
                        resume.setFilters(new ItemFilter[]{ItemFilter.IsResumable});
                        resume.setSortBy(new String[]{ItemSortBy.DatePlayed});
                        resume.setSortOrder(SortOrder.Descending);
                        mRows.add(new BrowseRowDef(mApplication.getString(R.string.lbl_continue_watching), resume, 0));
                    }

                    StdItemQuery latest = new StdItemQuery();
                    latest.setParentId(mFolder.getId());
                    latest.setLimit(50);
                    latest.setFilters(new ItemFilter[]{ItemFilter.IsUnplayed});
                    latest.setSortBy(new String[]{ItemSortBy.DateCreated});
                    latest.setSortOrder(SortOrder.Descending);
                    mRows.add(new BrowseRowDef(mApplication.getString(R.string.lbl_latest_additions), latest, 0));

                }


                StdItemQuery byName = new StdItemQuery();
                byName.setParentId(mFolder.getId());
                mRows.add(new BrowseRowDef(mApplication.getString(R.string.lbl_by_name), byName, 100));

                rowLoader.loadRows(mRows);

            }

        }

    }


}
