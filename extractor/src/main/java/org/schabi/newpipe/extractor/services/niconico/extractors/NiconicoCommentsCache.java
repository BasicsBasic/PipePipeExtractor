package org.schabi.newpipe.extractor.services.niconico.extractors;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import com.grack.nanojson.JsonWriter;

import edu.umd.cs.findbugs.annotations.NonNull;

import static org.schabi.newpipe.extractor.utils.Utils.UTF_8;

public class NiconicoCommentsCache {
    String id;
    JsonObject[] comments;

    public JsonObject[] getComments(@NonNull final JsonObject watch,
                                    final Downloader downloader,
                                    final String lastId) throws ExtractionException {
        if (Objects.equals(this.id, lastId)) {
            return comments;
        }
        this.id = lastId;
        JsonObject nvComment = watch.getObject("comment").getObject("nvComment");
        String url = "https://nv-comment.nicovideo.jp/v1/threads";

        final Map<String, List<String>> headers = new HashMap<>();
        headers.put("x-frontend-id", Collections.singletonList("6"));
        final String commentResponse;
        try {
            commentResponse = downloader.post(url, headers, JsonWriter.string(nvComment).getBytes(UTF_8)).responseBody();
        } catch (IllegalArgumentException | IOException e) {
            throw new ExtractionException(
                    "Could not get comments. Url: "
                            + url
                            + ", Thread: "
                            + JsonWriter.string(nvComment), e);
        }

        try {
            final JsonArray result = JsonParser.object().from(commentResponse).getObject("data").getArray("threads")
                    .getObject(1).getArray("comments");
            final List<JsonObject> commentsList = Arrays.stream(result.toArray())
                    .map(s -> (JsonObject) s)
                    .collect(Collectors.toList());
            // Reverse the order to show comments in order of newest to oldest.
            Collections.reverse(commentsList);
            comments = commentsList.toArray(new JsonObject[0]);
            return comments;
        } catch (final JsonParserException e) {
            throw new ParsingException("Could not parse comment json data. Response: "
                    + commentResponse, e);
        }
    }
}
