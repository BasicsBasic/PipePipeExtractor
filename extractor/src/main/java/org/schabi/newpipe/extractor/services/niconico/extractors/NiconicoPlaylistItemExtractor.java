package org.schabi.newpipe.extractor.services.niconico.extractors;

import com.grack.nanojson.JsonObject;

import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.localization.DateWrapper;
import org.schabi.newpipe.extractor.services.niconico.NiconicoService;
import org.schabi.newpipe.extractor.stream.StreamInfoItemExtractor;
import org.schabi.newpipe.extractor.stream.StreamType;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.annotation.Nullable;

public class NiconicoPlaylistItemExtractor implements StreamInfoItemExtractor {
    protected final JsonObject item;

    public NiconicoPlaylistItemExtractor(JsonObject item) {
        this.item = item.getObject("video");
    }

    @Override
    public String getName() throws ParsingException {
        return item.getString("title");
    }

    @Override
    public String getUrl() throws ParsingException {
        return NiconicoService.SP_WATCH_URL + item.getString("id");
    }

    @Override
    public String getThumbnailUrl() throws ParsingException {
        return item.getObject("thumbnail").getString("url");
    }

    @Override
    public StreamType getStreamType() throws ParsingException {
        return StreamType.VIDEO_STREAM;
    }

    @Override
    public boolean isAd() throws ParsingException {
        return false;
    }

    @Override
    public long getDuration() throws ParsingException {
        return item.getLong("duration");
    }

    @Override
    public long getViewCount() throws ParsingException {
        return item.getObject("count").getLong("view");
    }

    @Override
    public String getUploaderName() throws ParsingException {
        return item.getObject("owner").getString("name");
    }

    @Override
    public String getUploaderUrl() throws ParsingException {
        return NiconicoService.USER_URL + item.getObject("owner").getString("id");
    }

    @Nullable
    @Override
    public String getUploaderAvatarUrl() throws ParsingException {
        return item.getObject("owner").getString("iconUrl");
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        return false;
    }

    @Nullable
    @Override
    public String getTextualUploadDate() throws ParsingException {
        return item.getString("registeredAt").replace("T", " ").split("\\+")[0];
    }

    @Nullable
    @Override
    public DateWrapper getUploadDate() throws ParsingException {
        return new DateWrapper(LocalDateTime.parse(
                getTextualUploadDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atOffset(ZoneOffset.ofHours(+9)));
    }

}
