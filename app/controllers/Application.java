package controllers;

import constants.Constants;
import models.AndroidApp;
import models.WebPage;
import models.errors.NoAppFoundException;
import models.errors.ServerOverloadedException;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import webservice.WebService;

import java.util.*;

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
        WebPage webpage = new WebPage();

        String[] ids = {"com.snapchat.android","com.dropbox.android", "com.gente.radio","com.facebook.orca","com.facebook.katana","com.pandora.android","com.instagram.android","com.mobilemotion.dubsmash","com.whatsapp","com.supercell.clashofclans","com.miniclip.eightballpool","kik.android","com.prettysimple.criminalcaseandroid","tv.twitch.android.app","com.yodo1.crossyroad","com.cleanmaster.mguard","com.netflix.mediaclient","com.kiloo.subwaysurf","com.timgames.findthedifferencetim2","com.musicplayer.music","com.appypie.gb33bc7565573appypie.RunningtowardsBelize","com.wb.goog.mkx","com.skype.raider","com.kathleenOswald.solitaireGooglePlay","net.zedge.android","com.google.android.apps.inbox","com.weather.Weather","com.clearchannel.iheartradio.controller","com.soundcloud.android","com.twitter.android","com.king.candycrushsodasaga","com.emoji.coolkeyboard","com.amazon.mShop.android.shopping","com.contextlogic.wish","com.nordcurrent.canteenhd","com.shakespeare.slots.android","com.gamelion.cats","com.cleanmaster.security","com.mgc.miami.crime.simulator","com.quizup.core","com.google.android.play.games","com.imangi.templerun2","com.supercell.boombeach","com.ludia.jurassicworld","com.notdoppler.earntodie2","com.robtopx.geometryjumplite","com.yahoo.mobile.client.android.mail","com.king.candycrushsaga","com.microsoft.office.outlook","com.igg.castleclash","com.spaceapegames.rivalkingdoms","com.gamevil.dragonblaze1.android.google.global.normal","com.sgiggle.production","com.kiwi.skullislandexplorers","com.acmeaom.android.myradar","com.roostergames.hillclimbtruckracing3","com.gameloft.android.ANMP.GloftDMHM","com.ubercab","com.machinezone.gow","com.ebay.mobile","com.netmarble.mherosgb","com.halfbrick.fruitninjafree","com.oovoo","com.oceanview.twenty48reborn","com.jb.emoji.gokeyboard","com.etermax.preguntados.lite","com.wallapop","com.xmatch.cookiestory2","com.sgn.pandapop.gp","com.king.farmheroessaga","es.socialpoint.DragonCity","com.fingersoft.hillclimb","com.sec.android.easyMover","com.shazam.android","com.tinder","com.ludia.waldo3free","com.aim.racing","com.econdevpros.games.asteroid","com.eliferun.music","air.com.sgn.cookiejam.gp","co.vine.android","com.fivestargames.slots","com.explorationbase.ExplorationLite","com.sausageparty.superh2","com.umonistudio.tile","com.waze","com.igg.bzbee.deckheroes","com.smule.magicpiano","com.dianxinos.dxbs","com.ea.games.simsfreeplay_na","com.ijinshan.kbatterydoctor_en","jp.naver.line.android","com.outfit7.mytalkingtomfree","com.offerup","com.fgol.HungrySharkEvolution","com.yelp.android","com.cheerfulinc.flipagram","jp.co.hangame.sj8bitprpg","com.google.android.apps.translate","com.hulu.plus","com.walmart.android","com.picsart.studio","audio.mp3.music.player","com.leftover.CoinDozer","com.teamlava.dragon44","com.mudloop.zigzagboom"};

        F.Promise<List<AndroidApp>> promise = wb.getAppDetails(Arrays.asList(ids), Constants.Weight.LIGHT);
        return promise.map(playApps -> {

            return ok((play.twirl.api.Html) views.html.index.render(webpage, playApps));
        });
    }

    /**
     * Application page where we find information about it
     * @return html result
     */
    public F.Promise<Result> details(String id)
            throws NoAppFoundException, ServerOverloadedException
    {
        WebPage webpage = new WebPage();

        F.Promise<AndroidApp> promise = wb.getAppDetails(id, Constants.Weight.FULL);
        F.Promise<Map<Integer, List<String>>> topics = wb.getTopicTitles();

        return promise.flatMap(app -> {

            List<String> ids = new LinkedList<>(app.getRelatedIDs());
            if (ids.size() > 5) {
                ids = ids.subList(0, 5);
            }

            return wb.getAppDetails(ids, Constants.Weight.LIGHT).flatMap(apps -> {
                return topics.map(titles -> {

                    webpage.addPath(routes.Application.index(), app.getCategory().getTitle());
                    webpage.addPath(routes.Application.details(app.getAppID()), app.getName());

                    return ok((play.twirl.api.Html) views.html.details.render(webpage, app, titles, apps));
                });
            });
        });
    }

}
