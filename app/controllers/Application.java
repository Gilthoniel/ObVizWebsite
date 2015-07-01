package controllers;

import models.PlayApp;
import play.Logger;
import play.Play;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import webservice.WebService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Application extends Controller {

    private WebService wb;

    public Application() {

        wb = WebService.getInstance();
    }

    /**
     * Homepage
     * @return html result
     */
    public F.Promise<Result> index() {

        String[] ids = {"com.snapchat.android","com.gente.radio","com.facebook.orca","com.facebook.katana","com.pandora.android","com.instagram.android","com.mobilemotion.dubsmash","com.whatsapp","com.supercell.clashofclans","com.miniclip.eightballpool","kik.android","com.prettysimple.criminalcaseandroid","tv.twitch.android.app","com.yodo1.crossyroad","com.cleanmaster.mguard","com.netflix.mediaclient","com.kiloo.subwaysurf","com.timgames.findthedifferencetim2","com.musicplayer.music","com.appypie.gb33bc7565573appypie.RunningtowardsBelize","com.wb.goog.mkx","com.skype.raider","com.kathleenOswald.solitaireGooglePlay","net.zedge.android","com.google.android.apps.inbox","com.weather.Weather","com.clearchannel.iheartradio.controller","com.soundcloud.android","com.twitter.android","com.king.candycrushsodasaga","com.emoji.coolkeyboard","com.amazon.mShop.android.shopping","com.contextlogic.wish","com.nordcurrent.canteenhd","com.shakespeare.slots.android","com.gamelion.cats","com.cleanmaster.security","com.mgc.miami.crime.simulator","com.quizup.core","com.google.android.play.games","com.imangi.templerun2","com.supercell.boombeach","com.ludia.jurassicworld","com.notdoppler.earntodie2","com.robtopx.geometryjumplite","com.yahoo.mobile.client.android.mail","com.king.candycrushsaga","com.microsoft.office.outlook","com.igg.castleclash","com.spaceapegames.rivalkingdoms","com.gamevil.dragonblaze1.android.google.global.normal","com.sgiggle.production","com.kiwi.skullislandexplorers","com.acmeaom.android.myradar","com.roostergames.hillclimbtruckracing3","com.gameloft.android.ANMP.GloftDMHM","com.ubercab","com.machinezone.gow","com.ebay.mobile","com.netmarble.mherosgb","com.halfbrick.fruitninjafree","com.oovoo","com.oceanview.twenty48reborn","com.jb.emoji.gokeyboard","com.etermax.preguntados.lite","com.wallapop","com.xmatch.cookiestory2","com.sgn.pandapop.gp","com.king.farmheroessaga","es.socialpoint.DragonCity","com.fingersoft.hillclimb","com.sec.android.easyMover","com.shazam.android","com.tinder","com.ludia.waldo3free","com.aim.racing","com.econdevpros.games.asteroid","com.eliferun.music","air.com.sgn.cookiejam.gp","co.vine.android","com.fivestargames.slots","com.explorationbase.ExplorationLite","com.sausageparty.superh2","com.umonistudio.tile","com.waze","com.igg.bzbee.deckheroes","com.smule.magicpiano","com.dianxinos.dxbs","com.ea.games.simsfreeplay_na","com.ijinshan.kbatterydoctor_en","jp.naver.line.android","com.outfit7.mytalkingtomfree","com.offerup","com.fgol.HungrySharkEvolution","com.yelp.android","com.cheerfulinc.flipagram","jp.co.hangame.sj8bitprpg","com.google.android.apps.translate","com.hulu.plus","com.walmart.android","com.picsart.studio","audio.mp3.music.player","com.leftover.CoinDozer","com.teamlava.dragon44","com.mudloop.zigzagboom"};


        List<F.Promise<PlayApp>> apps = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            apps.add(wb.getAppInfo(ids[i]));
        }

        return F.Promise.sequence(apps).map(playApps -> {
            List<PlayApp> list = new ArrayList<>(playApps);
            Iterator<PlayApp> it = list.iterator();
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                }
            }

            return ok(views.html.index.render(list));
        });
    }

    /**
     * Application page where we find information about it
     * @return html result
     */
    public Result details(String id) {

        return ok(views.html.details.render(id));
    }

}
