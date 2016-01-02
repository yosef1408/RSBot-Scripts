package xXTheAwesomerXx.dbscripts.rs3.woodcutting;

import org.powerbot.script.AbstractQuery;
import org.powerbot.script.Condition;
import org.powerbot.script.StringUtils;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The Trade class offers method for offering, accepting, and validating trades.
 *
 * @author Coma
 */
public class Trade extends AbstractQuery<Trade, Item, ClientContext> {
	public final int OFFER_WIDGET = 335, OFFER_COMPONENT = 33,
			OFFER_PARTNER_COMPONENT = 10;
	public static final int REVIEW_WIDGET = 334, REVIEW_COMPONENT = 14,
			REVIEW_PARTNER_COMPONENT = 21;
	public static final int BACKPACK_WIDGET = 336, BACKPACK_COMPONENT = 0;
	public static final int OFFER_INDEX = 26, RECEIVING_INDEX = 29,
			OFFER_ACCEPT_BUTTON = 63, OFFER_DECLINE_BUTTON = 69,
			REVIEW_ACCEPT_BUTTON = 49, REVIEW_DECLINE_BUTTON = 54;
	public static final int OFFER_VALUE = 46, RECEIVING_VALUE = 47;
	public static final int CHAT_WIDGET = 137, CHAT_SCROLL_BAR = 133,
			CHAT_HISTORY_WIDGET = 132;
	public static final String INCOMING_TRADE = " wishes to trade with you.";
	public static final Pattern INCOMING_TRADE_PATTERN = Pattern
			.compile("(.+?)" + INCOMING_TRADE);
	public final Components components;

	public enum TradeWindow {
		OFFER, RECEIVING
	}

	public Trade(final ClientContext ctx) {
		super(ctx);
		this.components = new Components(ctx);
	}

	@Override
	protected Trade getThis() {
		return this;
	}

	@Override
	public Item nil() {
		return ctx.backpack.nil();
	}

	public Trade select(final TradeWindow window) {
		return select(get(window));
	}

	@Override
	protected List<Item> get() {
		return get(TradeWindow.OFFER);
	}

	protected List<Item> get(final TradeWindow window) {
		List<Item> list = new ArrayList<Item>();
		if (!trading()) {
			return list;
		}
		final Component items = ctx.widgets
				.component(
						OFFER_WIDGET,
						window == TradeWindow.OFFER ? OFFER_INDEX
								: window == TradeWindow.RECEIVING ? RECEIVING_INDEX
										: 0);
		for (Component item : items.components()) {
			list.add(new Item(ctx, item.itemId(), item.itemStackSize(), item));
		}
		return list;
	}

	/**
	 * Accepts an incoming trade offer by clicking the
	 * "[player] wishes to trade with you" message
	 *
	 * @param allowedPlayer
	 *            name of player that is acceptable to trade with, if specified.
	 *            if null or an empty string, it accepts all trades
	 * @return true if successfully initiated trade, false otherwise
	 */
	public boolean acceptIncomingTradeOffer(final String allowedPlayer) {
		this.components.select(CHAT_WIDGET, CHAT_HISTORY_WIDGET);
		if (allowedPlayer == null || allowedPlayer.isEmpty()) {
			this.components.text(INCOMING_TRADE_PATTERN);
		} else {
			this.components.text(allowedPlayer.concat(INCOMING_TRADE));
		}

		if (this.components.isEmpty()) {
			return false;
		}
		final Component c = this.components.poll();
		final Component scrollBar = ctx.widgets.component(CHAT_WIDGET,
				CHAT_SCROLL_BAR).component(1);
		ctx.widgets.scroll(c, scrollBar, false);
		return c.click() && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return trading();
			}
		}, 250, 10);
	}

	public boolean acceptIncomingTradeOffer() {
		return acceptIncomingTradeOffer(null);
	}

	/**
	 * Determines if you are currently trading with someone
	 *
	 * @return true if currently trading, false otherwise
	 */
	public boolean trading() {
		final Component c = ctx.widgets
				.component(OFFER_WIDGET, OFFER_COMPONENT);
		final Component c2 = ctx.widgets.component(REVIEW_WIDGET,
				REVIEW_COMPONENT);
		return ((c.valid() && c.visible()) || c2.valid() && c2.visible());
	}

	public boolean offering() {
		final Component c = ctx.widgets
				.component(OFFER_WIDGET, OFFER_COMPONENT);
		return c.valid() && c.visible();
	}

	public boolean reviewing() {
		final Component c = ctx.widgets.component(REVIEW_WIDGET,
				REVIEW_COMPONENT);
		return c.valid() && c.visible();
	}

	/**
	 * Clicks the "Accept" button in the offer interface
	 *
	 * @return true if you are currently trading and the the button was clicked,
	 *         false otherwise
	 */
	public boolean acceptOffer() {
		if (!trading()) {
			return false;
		}
		final Component button = ctx.widgets.component(OFFER_WIDGET,
				OFFER_ACCEPT_BUTTON);
		return button.visible() && button.click();
	}

	/**
	 *
	 * @return true if player has already accepted the trade offer
	 */
	public boolean acceptedOffer() {
		if (!trading()) {
			return false;
		}
		final Component button = ctx.widgets.component(OFFER_WIDGET,
				OFFER_ACCEPT_BUTTON);
		final Component acceptedMessage = ctx.widgets.component(OFFER_WIDGET,
				OFFER_COMPONENT);
		return button.visible() && acceptedMessage.text().contains("Waiting");
	}

	/**
	 * Clicks the "Accept" button in the review interface
	 *
	 * @return true if you are currently trading and the the button was clicked,
	 *         false otherwise
	 */
	public boolean acceptReview() {
		if (!trading()) {
			return false;
		}
		final Component button = ctx.widgets.component(REVIEW_WIDGET,
				REVIEW_ACCEPT_BUTTON);
		return button.visible() && button.click();
	}

	/**
	 *
	 * @return true if player has already accepted the trade review
	 */
	public boolean acceptedReview() {
		if (!trading()) {
			return false;
		}
		final Component button = ctx.widgets.component(REVIEW_WIDGET,
				REVIEW_ACCEPT_BUTTON);
		final Component acceptedMessage = ctx.widgets.component(REVIEW_WIDGET,
				REVIEW_COMPONENT);
		return button.visible() && acceptedMessage.text().contains("Waiting");
	}

	/**
	 * Clicks the "Decline" button in the trade interface
	 *
	 * @return true if you are currently trading and the the button was clicked
	 *         and successfully exited trade, false otherwise
	 */
	public boolean declineOffer() {
		if (!trading()) {
			return false;
		}
		final Component button = ctx.widgets.component(OFFER_WIDGET,
				OFFER_DECLINE_BUTTON);
		return button.visible() && button.click()
				&& Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return !trading();
					}
				}, 250, 10);
	}

	public boolean declineReview() {
		if (!trading()) {
			return false;
		}
		final Component button = ctx.widgets.component(REVIEW_WIDGET,
				REVIEW_DECLINE_BUTTON);
		return button.visible() && button.click()
				&& Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return !trading();
					}
				}, 250, 10);
	}

	public boolean partnerAcceptedOffer() {
		final Component c = ctx.widgets
				.component(OFFER_WIDGET, OFFER_COMPONENT);
		return c.text().contains("accepted");
	}

	public boolean partnerAcceptedReview() {
		final Component c = ctx.widgets.component(REVIEW_WIDGET,
				REVIEW_COMPONENT);
		return c.text().contains("accepted");
	}

	/**
	 * Gets the name of the player that you are currently trading
	 *
	 * @return returns an empty string if you are not trading with anyone;
	 *         otherwise returns the name of the player you are trading
	 */
	public String getTradePartner() {
		if (!trading()) {
			return "";
		}
		final Component c = ctx.widgets.widget(OFFER_WIDGET).component(
				OFFER_PARTNER_COMPONENT);
		final Component c2 = ctx.widgets.widget(REVIEW_WIDGET).component(
				REVIEW_PARTNER_COMPONENT);
		if (c.visible()) {
			return c.text();
		} else {
			return c2.text();
		}
	}

	private int getWealthTransfer(final int index) {
		final Component c = ctx.widgets.component(OFFER_WIDGET, index);
		final String text = c.text().replaceAll("\\D", "");
		return text.isEmpty() ? 0 : StringUtils.parseInt(text);
	}

	/**
	 * @return returns the total value of items that you are offering
	 */
	public int getOfferValue() {
		return getWealthTransfer(OFFER_VALUE);
	}

	/**
	 * @return returns the total value of items that you are receiving
	 */
	public int getReceivingValue() {
		return getWealthTransfer(RECEIVING_VALUE);
	}

	/**
	 * @return returns the difference between what you are offering and what you
	 *         are receiving
	 */
	public int getWealthTransferValue() {
		return getReceivingValue() - getOfferValue();
	}

	public int itemIdAt(int index) {
		if (!trading()) {
			return 0;
		}
		return ctx.widgets.widget(336).component(0).component(index).itemId();
	}
	
	public String itemNameAt(int index) {
		if (!trading()) {
			return "";
		}
		return ctx.widgets.widget(336).component(0).component(index).itemName();
	}//trade based on backpack index
	
}