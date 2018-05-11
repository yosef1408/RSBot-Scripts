package sintaax.tutorialisland.engine.constants;

import sintaax.tutorialisland.engine.objects.Context;

import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Npc;

public class Components extends Context<ClientContext> {
    public Components(ClientContext ctx) {
        super(ctx);
    }

    public final Component ERROR_CONTINUE_BUTTON = ctx.widgets.component(162, 37);
    public final Component OPTIONS_BUTTON = ctx.widgets.component(548, 42);
    public final Component OPTIONS_WINDOW = ctx.widgets.component(548, 77);
    public final Component AUDIO_BUTTON = ctx.widgets.component(261, 1).component(2);
    public final Component AUDIO_WINDOW = ctx.widgets.component(261, 22);
    public final Component AUDIO_OFF_BUTTON = ctx.widgets.component(261, 24);
    public final Component DISPLAY_BUTTON = ctx.widgets.component(261, 1).component(0);
    public final Component DISPLAY_WINDOW = ctx.widgets.component(261, 3);
    public final Component DISPLAY_ADVANCED_BUTTON = ctx.widgets.component(261,21);
    public final Component DISPLAY_ADVANCED_WINDOW = ctx.widgets.component(60, 1);
    public final Component ROOF_BUTTON = ctx.widgets.component(60, 14);
    public final Component BACKPACK_BUTTON = ctx.widgets.component(548, 51);
    public final Component BACKPACK_WINDOW = ctx.widgets.component(548, 69);
    public final Component STATS_BUTTON = ctx.widgets.component(548, 56);
    public final Component MUSICPLAYER_BUTTON = ctx.widgets.component(548, 44);
    public final Component EMOTE_BUTTON = ctx.widgets.component(548, 43);
    public final Component EMOTE_WINDOW = ctx.widgets.component(548, 78);
    public final Component EMOTE_RANDOM = ctx.widgets.component(216, 1).component(Random.nextInt(0, 21));
    public final Component RUN_BUTTON = ctx.widgets.component(160, 24);
    public final Component QUESTLIST_BUTTON = ctx.widgets.component(548, 57);
    public final Component SMITHING_WINDOW = ctx.widgets.component(312, 1);
    public final Component SMITHING_DAGGER_BUTTON = ctx.widgets.component(312, 2).component(2);
    public final Component WORNEQUIPMENT_BUTTON = ctx.widgets.component(548, 59);
    public final Component WORNEQUIPMENT_WINDOW = ctx.widgets.component(387, 0);
    public final Component EQUIPMENTSTATS_BUTTON = ctx.widgets.component(387, 17);
    public final Component EQUIPMENTSTATS_WINDOW = ctx.widgets.component(84, 1);
    public final Component EQUIPMENTSTATS_CLOSE_BUTTON = ctx.widgets.component(84, 4);
    public final Component COMBATOPTIONS_BUTTON = ctx.widgets.component(548, 55);
    public final Component MAINHAND_SLOT = ctx.widgets.component(387, 9);
    public final Component ARROWS_SLOT = ctx.widgets.component(387, 16);
    public final Component CONTINUE_BUTTON = ctx.widgets.component(193, 2);
    public final Component BANK_WINDOW = ctx.widgets.component(12, 14).component(0);
    public final Component BANK_CLOSE_BUTTON = ctx.widgets.component(12, 14).component(11);
    public final Component PRAYER_BUTTON = ctx.widgets.component(548, 53);
    public final Component FRIENDS_BUTTON = ctx.widgets.component(548, 39);
    public final Component IGNORED_BUTTON = ctx.widgets.component(548, 40);
    public final Component MAGIC_BUTTON = ctx.widgets.component(548, 61);
    public final Component MAGIC_WINDOW = ctx.widgets.component(218, 0);
    public final Component MAGIC_HOMETELEPORT_BUTTON = ctx.widgets.component(218, 1);
    public final Component MAGIC_WINDSTRIKE_BUTTON = ctx.widgets.component(218, 2);
    public final Component SYSTEMLINE_FIRST = ctx.widgets.component(162, 47).component(0);
    public final Component CHATLINE_FIRST = ctx.widgets.component(162, 47).component(1);
    public final Component SYSTEMLINE_SECOND = ctx.widgets.component(162, 47).component(2);
    public final Component CHATLINE_SECOND = ctx.widgets.component(162, 47).component(3);
    public final Component SYSTEMLINE_THIRD = ctx.widgets.component(162, 47).component(4);
    public final Component CHATLINE_THIRD = ctx.widgets.component(162, 47).component(5);
    public final Component SYSTEMLINE_FOURTH = ctx.widgets.component(162, 47).component(6);
    public final Component CHATLINE_FOURTH = ctx.widgets.component(162, 47).component(7);
    public final Component SYSTEMLINE_FIFTH = ctx.widgets.component(162, 47).component(8);
    public final Component CHATLINE_FIFTH = ctx.widgets.component(162, 47).component(9);
    public final Component SYSTEMLINE_SIXTH = ctx.widgets.component(162, 47).component(10);
    public final Component CHATLINE_SIXTH = ctx.widgets.component(162, 47).component(11);
    public final Component SYSTEMLINE_SEVENTH = ctx.widgets.component(162, 47).component(12);
    public final Component CHATLINE_SEVENTH = ctx.widgets.component(162, 47).component(13);
    public final Component SYSTEMLINE_EIGHTH = ctx.widgets.component(162, 47).component(14);
    public final Component CHATLINE_EIGHTH = ctx.widgets.component(162, 47).component(15);


    public void openWindow(Component window, Component button) {
        if (!window.visible())
            button.click();
    }

    public boolean canHomeTeleport() {
        for (int x = 0; x < 15; x += 2) {
            if (ctx.widgets.component(162, 47).component(x).text().contains("You need to wait"))
                return false;
        }

        return true;
    }

    public boolean parseSystemMessage(String string) {
        for (int x = 0; x < 15; x += 2) {
            if (ctx.widgets.component(162, 47).component(x).text().contains(string))
                return true;
        }

        return false;
    }

    public boolean parseChatMessage(String string) {
        for (int x = 1; x < 15; x += 2) {
            if (ctx.widgets.component(162, 47).component(x).text().contains(string))
                return true;
        }

        return false;
    }

    public void cast(Component spell, Npc target) {
        if (MAGIC_WINDOW.visible()) {
            spell.click();
            ctx.camera.turnTo(target);
            target.interact("Cast");
        }
        else
            MAGIC_BUTTON.click();
    }
}
