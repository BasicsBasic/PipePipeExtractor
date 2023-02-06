package org.schabi.newpipe.extractor.services.peertube.search.filter;


import org.schabi.newpipe.extractor.search.filter.Filter;
import org.schabi.newpipe.extractor.search.filter.FilterGroup;
import org.schabi.newpipe.extractor.search.filter.FilterItem;
import org.schabi.newpipe.extractor.search.filter.SearchFiltersBase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class PeertubeFilters extends SearchFiltersBase {

    private boolean isAscending = false;

    public PeertubeFilters() {
        init();
        build();
    }

    @Override
    public String evaluateSelectedFilters(final String searchString) {
        if (selectedSortFilter != null) {
            final StringBuilder sortQuery = new StringBuilder();

            final Optional<FilterItem> ascendingFilter = selectedSortFilter.stream()
                    .filter(filterItem -> filterItem instanceof PeertubeSortOrderFilterItem)
                    .findFirst();
            isAscending = ascendingFilter.isPresent();
            for (final FilterItem item : selectedSortFilter) {
                appendFilterToQueryString(item, sortQuery);
            }

            if (selectedContentFilter != null && !selectedContentFilter.isEmpty()) {
                appendFilterToQueryString(selectedContentFilter.get(0), sortQuery);
            }

            return sortQuery.toString();
        }

        return "";
    }

    @Override
    protected void init() {
        /* content filters */
        final int contentFilterAll = builder.addSortItem(
                new PeertubeFilterItem("All", ""));
        final int contentFilterVideos = builder.addSortItem(
                new PeertubeFilterItem("Videos", "resultType=videos"));
        final int contentFilterChannels = builder.addSortItem(
                new PeertubeFilterItem("Channels", "resultType=channels"));
        final int contentFilterPlaylists = builder.addSortItem(
                new PeertubeFilterItem("Playlists", "resultType=playlists"));


        /* this is the default content filter */
        this.defaultContentFilterId = contentFilterAll;

        /* content filters */
        addContentFilter(builder.createSortGroup(null, true, new FilterItem[]{
                builder.getFilterForId(contentFilterAll),
                builder.getFilterForId(contentFilterVideos),
                builder.getFilterForId(contentFilterChannels),
                builder.getFilterForId(contentFilterPlaylists),
        }));

        final int filterSepiaSearch = builder.addSortItem(
                new PeertubeSepiaFilterItem("SepiaSearch"));

        /* 'Sort by' filter items */
        final int filterDateRelevance = builder.addSortItem(
                new PeertubeSortFilterItem("Relevance", "sort=match"));
        final int filterDateName = builder.addSortItem(
                new PeertubeSortFilterItem("Name", "sort=name"));
        final int filterDateDuration = builder.addSortItem(
                new PeertubeSortFilterItem("Duration", "sort=duration"));
        final int filterDatePublishedAt = builder.addSortItem(
                new PeertubeSortFilterItem("Publish date", "sort=publishedAt"));
        final int filterDateCreatedAt = builder.addSortItem(
                new PeertubeSortFilterItem("Creation date", "sort=createdAt"));
        final int filterDateViews = builder.addSortItem(
                new PeertubeSortFilterItem("Views", "sort=views"));
        final int filterDateLikes = builder.addSortItem(
                new PeertubeSortFilterItem("Likes", "sort=likes"));

        final int filterDateSortOrder = builder.addSortItem(
                new PeertubeSortOrderFilterItem("Ascending"));

        /* stream kind filter items */
        final int filterVideoAll = builder.addSortItem(
                new PeertubeFilterItem("All", ""));
        final int filterVideoLiveOnly = builder.addSortItem(
                new PeertubeFilterItem("Live", "isLive=true"));
        final int filterVideoVODsOnly = builder.addSortItem(
                new PeertubeFilterItem("VOD videos", "isLive=false"));

        /* sensitive filter items */
        final int filterSensitiveContentAll = builder.addSortItem(
                new PeertubeFilterItem("All", ""));
        final int filterSensitiveContentYes = builder.addSortItem(
                new PeertubeFilterItem("Yes", "nsfw=both"));
        final int filterSensitiveContentNo = builder.addSortItem(
                new PeertubeFilterItem("No", "nsfw=false"));

        /* 'Date' filter items */
        // here query is set to null as the value is generated dynamically
        final int filterPublishedDateAll = builder.addSortItem(
                new PeertubePublishedDateFilterItem("All", null,
                        PeertubePublishedDateFilterItem.NO_DAYS_SET));
        final int filterPublishedDateToday = builder.addSortItem(
                new PeertubePublishedDateFilterItem("Today", null, 1));
        final int filterPublishedDateLast7Days = builder.addSortItem(
                new PeertubePublishedDateFilterItem("last 7 days", null, 7));
        final int filterPublishedDateLast30Days = builder.addSortItem(
                new PeertubePublishedDateFilterItem("last 30 days", null, 30));
        final int filterPublishedDateLastYear = builder.addSortItem(
                new PeertubePublishedDateFilterItem("last year", null, 365));

        /* 'Duration' filter items */
        final int filterDurationAll = builder.addSortItem(
                new PeertubeFilterItem("All", ""));
        final int filterDurationShort = builder.addSortItem(
                new PeertubeFilterItem("Short (< 4 min)", "durationMax=240"));
        final int filterDurationMedium = builder.addSortItem(
                new PeertubeFilterItem("Medium (4-10 min)", "durationMin=240&durationMax=600"));
        final int filterDurationLong = builder.addSortItem(
                new PeertubeFilterItem("Long (> 10 min)", "durationMin=600"));

        final Filter allSortFilters = new Filter.Builder(new FilterGroup[]{
                builder.createSortGroup(null, false, new FilterItem[]{
                        builder.getFilterForId(filterSepiaSearch),
                }),
                builder.createSortGroup("Sort by", true, new FilterItem[]{
                        builder.getFilterForId(filterDateRelevance),
                        builder.getFilterForId(filterDateName),
                        builder.getFilterForId(filterDateDuration),
                        builder.getFilterForId(filterDatePublishedAt),
                        builder.getFilterForId(filterDateCreatedAt),
                        builder.getFilterForId(filterDateViews),
                        builder.getFilterForId(filterDateLikes),
                }),
                builder.createSortGroup("Sort order", false, new FilterItem[]{
                        builder.getFilterForId(filterDateSortOrder),
                }),
                builder.createSortGroup("Kind", true, new FilterItem[]{
                        builder.getFilterForId(filterVideoAll),
                        builder.getFilterForId(filterVideoLiveOnly),
                        builder.getFilterForId(filterVideoVODsOnly),
                }),
                builder.createSortGroup("Sensitive", true, new FilterItem[]{
                        builder.getFilterForId(filterSensitiveContentAll),
                        builder.getFilterForId(filterSensitiveContentYes),
                        builder.getFilterForId(filterSensitiveContentNo),
                }),
                builder.createSortGroup("Published", true, new FilterItem[]{
                        builder.getFilterForId(filterPublishedDateAll),
                        builder.getFilterForId(filterPublishedDateToday),
                        builder.getFilterForId(filterPublishedDateLast7Days),
                        builder.getFilterForId(filterPublishedDateLast30Days),
                        builder.getFilterForId(filterPublishedDateLastYear),
                }),
                builder.createSortGroup("Duration", true, new FilterItem[]{
                        builder.getFilterForId(filterDurationAll),
                        builder.getFilterForId(filterDurationShort),
                        builder.getFilterForId(filterDurationMedium),
                        builder.getFilterForId(filterDurationLong),
                }),
        }).build();

        final Filter sepiaFilterOnly = new Filter.Builder(new FilterGroup[]{
                builder.createSortGroup(null, false, new FilterItem[]{
                        builder.getFilterForId(filterSepiaSearch),
                })}).build();
        addContentFilterSortVariant(-1, allSortFilters);
        addContentFilterSortVariant(contentFilterAll, allSortFilters);
        addContentFilterSortVariant(contentFilterVideos, allSortFilters);
        addContentFilterSortVariant(contentFilterChannels, sepiaFilterOnly);
        addContentFilterSortVariant(contentFilterPlaylists, sepiaFilterOnly);
    }

    private void appendFilterToQueryString(final FilterItem item,
                                           final StringBuilder sortQuery) {
        if (item instanceof PeertubeFilterItem) {
            final PeertubeFilterItem sortItem =
                    (PeertubeFilterItem) item;
            final String query = sortItem.getQueryData();
            if (!query.isEmpty()) {
                sortQuery.append("&").append(query);
            }
        }
    }

    private static class PeertubeFilterItem extends FilterItem {
        protected final String query;

        PeertubeFilterItem(final String name, final String query) {
            super(Filter.ITEM_IDENTIFIER_UNKNOWN, name);
            this.query = query;
        }

        public String getQueryData() {
            return query;
        }
    }

    private static class PeertubePublishedDateFilterItem extends PeertubeFilterItem {
        private static final int NO_DAYS_SET = -1;
        private final int days;

        PeertubePublishedDateFilterItem(final String name, final String query, final int days) {
            super(name, query);
            this.days = days;
        }

        @Override
        public String getQueryData() {
            // return format eg: startDate=2022-08-01T22:00:00.000

            if (days == NO_DAYS_SET) {
                return "";
            }
            final LocalDateTime localDateTime = LocalDateTime.now().minusDays(days);

            return "startDate=" + localDateTime.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        }
    }

    public static class PeertubeSepiaFilterItem extends FilterItem {
        public PeertubeSepiaFilterItem(final String name) {
            super(Filter.ITEM_IDENTIFIER_UNKNOWN, name);
        }
    }

    private static class PeertubeSortOrderFilterItem extends FilterItem {
        PeertubeSortOrderFilterItem(final String name) {
            super(Filter.ITEM_IDENTIFIER_UNKNOWN, name);
        }
    }

    private class PeertubeSortFilterItem extends PeertubeFilterItem {
        PeertubeSortFilterItem(final String name, final String query) {
            super(name, query);
        }

        @Override
        public String getQueryData() {
            if (!isAscending) {
                return query.replace("=", "=-");
            }
            return super.getQueryData();
        }
    }
}
