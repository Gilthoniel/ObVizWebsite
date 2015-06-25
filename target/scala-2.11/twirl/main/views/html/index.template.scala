
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object index_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._

class index extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.4*/("""

"""),_display_(/*3.2*/main()/*3.8*/ {_display_(Seq[Any](format.raw/*3.10*/("""
    """),format.raw/*4.57*/("""
    """),format.raw/*5.5*/("""<div class="row" id="home-searchbar">
        """),format.raw/*6.19*/("""
        """),format.raw/*7.9*/("""<div class="col-xs-12 logo">
            <img src=""""),_display_(/*8.24*/routes/*8.30*/.Assets.versioned("images/favicon.png")),format.raw/*8.69*/("""" alt="O" /> bviz
        </div>

        """),format.raw/*11.25*/("""
        """),format.raw/*12.9*/("""<div class="col-xs-12 col-md-6 col-md-offset-3">
            <div class="input-group">
                <input type="text" class="form-control" placeholder="Search for ..." />
                <span class="input-group-btn">
                    <button class="btn btn-primary" type="button">Go!</button>
                </span>
            </div>
        </div>

        """),format.raw/*21.26*/("""
        """),format.raw/*22.9*/("""<div class="col-xs-12 description">
            <p>
                Enter keywords or an app name of the Google Play Store.
            </p>
        </div>
    </div>

    """),format.raw/*29.31*/("""
    """),format.raw/*30.5*/("""<div class="container-fluid" id="list-applications">
        <h3>Popular</h3>
        <ul class="bxslider" id="popular-app">
            <li>
                <div class="app">
                    <dl class="header">
                        <dt><img src="https://lh6.ggpht.com/mp86vbELnqLi2FzvhiKdPX31_oiTRLNyeK8x4IIrbF5eD1D5RdnVwjQP0hwMNR_JdA=w300" alt="Logo" /></dt>
                        <dd><a href=""""),_display_(/*37.39*/routes/*37.45*/.Application.details("com.whatsapp")),format.raw/*37.81*/("""">Whatsapp Messenger</a></dd>
                        <dd>Whatsapp Inc.</dd>
                    </dl>

                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-xs-4 opinion-graph">
                            <h4>Battery</h4>
                            <canvas class="app-gauge" data-value="80" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Bugs</h4>
                            <canvas class="app-gauge" data-value="30" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Interface</h4>
                            <canvas class="app-gauge" data-value="50" height="30" width="70"></canvas>
                        </div>
                    </div>
                </div>
            </li>
            <li>
                <div class="app">
                    <dl class="header">
                        <dt><img src="https://lh3.googleusercontent.com/VU9JzTveNMwVZsFj4qJkrPmmmmmtYfWrxEkpYabPJKFFFFs-dOxsZAVjE7ghqfQOIA=w300" alt="Logo" /></dt>
                        <dd>iTube Pro</dd>
                        <dd>Reverse Clocking</dd>
                    </dl>

                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-xs-4 opinion-graph">
                            <h4>Cost</h4>
                            <canvas class="app-gauge" data-value="20" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Bugs</h4>
                            <canvas class="app-gauge" data-value="90" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Sound</h4>
                            <canvas class="app-gauge" data-value="60" height="30" width="70"></canvas>
                        </div>
                    </div>
                </div>
            </li>
            <li>
                <div class="app">
                    <dl class="header">
                        <dt><img src="https://lh6.ggpht.com/mp86vbELnqLi2FzvhiKdPX31_oiTRLNyeK8x4IIrbF5eD1D5RdnVwjQP0hwMNR_JdA=w300" alt="Logo" /></dt>
                        <dd>Whatsapp Messenger</dd>
                        <dd>Whatsapp Inc.</dd>
                    </dl>

                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-xs-4 opinion-graph">
                            <h4>Battery</h4>
                            <canvas class="app-gauge" data-value="80" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Bugs</h4>
                            <canvas class="app-gauge" data-value="30" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Interface</h4>
                            <canvas class="app-gauge" data-value="50" height="30" width="70"></canvas>
                        </div>
                    </div>
                </div>
            </li>
            <li>
                <div class="app">
                    <dl class="header">
                        <dt><img src="https://lh3.googleusercontent.com/VU9JzTveNMwVZsFj4qJkrPmmmmmtYfWrxEkpYabPJKFFFFs-dOxsZAVjE7ghqfQOIA=w300" alt="Logo" /></dt>
                        <dd>iTube Pro</dd>
                        <dd>Reverse Clocking</dd>
                    </dl>

                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-xs-4 opinion-graph">
                            <h4>Cost</h4>
                            <canvas class="app-gauge" data-value="20" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Bugs</h4>
                            <canvas class="app-gauge" data-value="90" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Sound</h4>
                            <canvas class="app-gauge" data-value="60" height="30" width="70"></canvas>
                        </div>
                    </div>
                </div>
            </li>
        </ul>

        <h3>New</h3>
        <ul class="bxslider" id="new-app">
            <li>
                <div class="app">
                    <dl class="header">
                        <dt><img src="https://lh6.ggpht.com/mp86vbELnqLi2FzvhiKdPX31_oiTRLNyeK8x4IIrbF5eD1D5RdnVwjQP0hwMNR_JdA=w300" alt="Logo" /></dt>
                        <dd>Whatsapp Messenger</dd>
                        <dd>Whatsapp Inc.</dd>
                    </dl>

                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-xs-4 opinion-graph">
                            <h4>Battery</h4>
                            <canvas class="app-gauge" data-value="80" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Bugs</h4>
                            <canvas class="app-gauge" data-value="30" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Interface</h4>
                            <canvas class="app-gauge" data-value="50" height="30" width="70"></canvas>
                        </div>
                    </div>
                </div>
            </li>
            <li>
                <div class="app">
                    <dl class="header">
                        <dt><img src="https://lh3.googleusercontent.com/VU9JzTveNMwVZsFj4qJkrPmmmmmtYfWrxEkpYabPJKFFFFs-dOxsZAVjE7ghqfQOIA=w300" alt="Logo" /></dt>
                        <dd>iTube Pro</dd>
                        <dd>Reverse Clocking</dd>
                    </dl>

                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-xs-4 opinion-graph">
                            <h4>Cost</h4>
                            <canvas class="app-gauge" data-value="20" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Bugs</h4>
                            <canvas class="app-gauge" data-value="90" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Sound</h4>
                            <canvas class="app-gauge" data-value="60" height="30" width="70"></canvas>
                        </div>
                    </div>
                </div>
            </li>
            <li>
                <div class="app">
                    <dl class="header">
                        <dt><img src="https://lh6.ggpht.com/mp86vbELnqLi2FzvhiKdPX31_oiTRLNyeK8x4IIrbF5eD1D5RdnVwjQP0hwMNR_JdA=w300" alt="Logo" /></dt>
                        <dd>Whatsapp Messenger</dd>
                        <dd>Whatsapp Inc.</dd>
                    </dl>

                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-xs-4 opinion-graph">
                            <h4>Battery</h4>
                            <canvas class="app-gauge" data-value="80" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Bugs</h4>
                            <canvas class="app-gauge" data-value="30" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Interface</h4>
                            <canvas class="app-gauge" data-value="50" height="30" width="70"></canvas>
                        </div>
                    </div>
                </div>
            </li>
            <li>
                <div class="app">
                    <dl class="header">
                        <dt><img src="https://lh3.googleusercontent.com/VU9JzTveNMwVZsFj4qJkrPmmmmmtYfWrxEkpYabPJKFFFFs-dOxsZAVjE7ghqfQOIA=w300" alt="Logo" /></dt>
                        <dd>iTube Pro</dd>
                        <dd>Reverse Clocking</dd>
                    </dl>

                    <div class="clearfix"></div>
                    <div class="row">
                        <div class="col-xs-4 opinion-graph">
                            <h4>Cost</h4>
                            <canvas class="app-gauge" data-value="20" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Bugs</h4>
                            <canvas class="app-gauge" data-value="90" height="30" width="70"></canvas>
                        </div>
                        <div class="col-xs-4 opinion-graph">
                            <h4>Sound</h4>
                            <canvas class="app-gauge" data-value="60" height="30" width="70"></canvas>
                        </div>
                    </div>
                </div>
            </li>
        </ul>
    </div>

    <script type="text/javascript">
        $(document).ready(function() """),format.raw/*241.38*/("""{"""),format.raw/*241.39*/("""
            """),format.raw/*242.13*/("""$(".bxslider").bxSlider("""),format.raw/*242.37*/("""{"""),format.raw/*242.38*/("""
                """),format.raw/*243.17*/("""slideMargin: 20,
                slideWidth: 300,
                maxSlides: 3,
                moveSlides: 1,
                pager: false
            """),format.raw/*248.13*/("""}"""),format.raw/*248.14*/(""");

            var opts = """),format.raw/*250.24*/("""{"""),format.raw/*250.25*/("""
                """),format.raw/*251.17*/("""lines: 12,
                angle: 0,
                lineWidth: 0.15,
                pointer: """),format.raw/*254.26*/("""{"""),format.raw/*254.27*/("""
                    """),format.raw/*255.21*/("""length: 0.4,
                    strokeWidth: 0.05,
                    color: '#000000'
                """),format.raw/*258.17*/("""}"""),format.raw/*258.18*/(""",
                limitMax: 'false',
                percentColors: [[0.0, "#cf1414" ], [0.5, "#aeaeae"], [1.0, "#1c8e17"]],
                strokeColor: '#e0e0e0',
                generateGradient: true
            """),format.raw/*263.13*/("""}"""),format.raw/*263.14*/(""";

            $(".app-gauge").each(function() """),format.raw/*265.45*/("""{"""),format.raw/*265.46*/("""
                """),format.raw/*266.17*/("""var gauge = new Gauge($(this).get(0)).setOptions(opts);
                gauge.maxValue = 100;
                gauge.animationSpeed = 1;
                gauge.set(parseInt($(this).data("value")));
            """),format.raw/*270.13*/("""}"""),format.raw/*270.14*/(""");
        """),format.raw/*271.9*/("""}"""),format.raw/*271.10*/(""");
    </script>
""")))}),format.raw/*273.2*/("""
"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


}

/**/
object index extends index_Scope0.index
              /*
                  -- GENERATED --
                  DATE: Thu Jun 25 08:35:49 CEST 2015
                  SOURCE: /home/gaylor/Developpement/ObViz/obviz-app/app/views/index.scala.html
                  HASH: ffaaf1721030542c35cebbbcf0c2896fde112762
                  MATRIX: 738->1|834->3|862->6|875->12|914->14|946->71|977->76|1050->132|1085->141|1163->193|1177->199|1236->238|1306->296|1342->305|1738->690|1774->699|1974->897|2006->902|2439->1308|2454->1314|2511->1350|12393->11203|12423->11204|12465->11217|12518->11241|12548->11242|12594->11259|12775->11411|12805->11412|12861->11439|12891->11440|12937->11457|13061->11552|13091->11553|13141->11574|13275->11679|13305->11680|13550->11896|13580->11897|13656->11944|13686->11945|13732->11962|13969->12170|13999->12171|14038->12182|14068->12183|14117->12201
                  LINES: 27->1|32->1|34->3|34->3|34->3|35->4|36->5|37->6|38->7|39->8|39->8|39->8|42->11|43->12|52->21|53->22|60->29|61->30|68->37|68->37|68->37|272->241|272->241|273->242|273->242|273->242|274->243|279->248|279->248|281->250|281->250|282->251|285->254|285->254|286->255|289->258|289->258|294->263|294->263|296->265|296->265|297->266|301->270|301->270|302->271|302->271|304->273
                  -- GENERATED --
              */
          