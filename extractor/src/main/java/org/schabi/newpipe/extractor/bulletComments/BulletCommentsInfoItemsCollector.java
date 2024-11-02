package org.schabi.newpipe.extractor.bulletComments;

import org.schabi.newpipe.extractor.InfoItemsCollector;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

public class BulletCommentsInfoItemsCollector
        extends InfoItemsCollector<BulletCommentsInfoItem, BulletCommentsInfoItemExtractor> {
    public BulletCommentsInfoItemsCollector(final int serviceId) {
        super(serviceId);
    }

    @Override
    public BulletCommentsInfoItem extract(final BulletCommentsInfoItemExtractor extractor)
            throws ParsingException {
        final BulletCommentsInfoItem resultItem = new BulletCommentsInfoItem(
                getServiceId(), extractor.getUrl(), extractor.getName());

        // optional information
        try {
            resultItem.setCommentText(extractor.getCommentText());
        } catch (final Exception e) {
            addError(e);
        }
        try {
            resultItem.setArgbColor(extractor.getArgbColor());
        } catch (final Exception e) {
            addError(e);
        }
        try {
            resultItem.setPosition(extractor.getPosition());
        } catch (final Exception e) {
            addError(e);
        }
        try {
            resultItem.setRelativeFontSize(extractor.getRelativeFontSize());
        } catch (final Exception e) {
            addError(e);
        }
        try{
            resultItem.setDuration(extractor.getDuration());
        } catch (final Exception e) {
            addError(e);
        }
        return resultItem;
    }
}
