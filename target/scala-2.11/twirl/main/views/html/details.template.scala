
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object details_Scope0 {
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

class details extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template1[String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(id: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.14*/("""

"""),_display_(/*3.2*/main()/*3.8*/ {_display_(Seq[Any](format.raw/*3.10*/("""
    """),format.raw/*4.5*/("""<div class="row" id="details-header">
        <div class="col-xs-12 col-md-6">
            <table class="logo">
                <tr>
                    <td>
                        <img src="https://lh6.ggpht.com/mp86vbELnqLi2FzvhiKdPX31_oiTRLNyeK8x4IIrbF5eD1D5RdnVwjQP0hwMNR_JdA=w300" alt="Logo" />
                    </td>
                    <td>
                        Whatsapp<br />
                        Whatsapp Inc.
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <div class="row" id="details-gauge">
        <div class="col-xs-12 col-md-3 active">
            <canvas width="220" height="100" data-value="30" class="gauge"></canvas>
            <h4>Battery</h4>
        </div>
        <div class="col-xs-12 col-md-3">
            <canvas width="220" height="100" data-value="90" class="gauge"></canvas>
            <h4>Performance</h4>
        </div>
        <div class="col-xs-12 col-md-3">
            <canvas width="220" height="100" data-value="50" class="gauge"></canvas>
            <h4>Design</h4>
        </div>
        <div class="col-xs-12 col-md-3">
            <canvas width="220" height="100" data-value="60" class="gauge"></canvas>
            <h4>S.T.</h4>
        </div>
    </div>

    <div class="row" id="details-opinions">

        <div class="col-xs-3">
            <div class="graph-opinions negative">
                <div class="opinion">
                    <span class="title">Low</span>
                    <span class="value" style="width: 100px;"></span>
                </div>
                <div class="opinion">
                    <span class="title">Heat</span>
                    <span class="value" style="width: 70px;"></span>
                </div>
                <div class="opinion">
                    <span class="title">Heat</span>
                    <span class="value" style="width: 70px;"></span>
                </div>
                <div class="opinion">
                    <span class="title">Heat</span>
                    <span class="value" style="width: 40px;"></span>
                </div>
                <div class="opinion">
                    <span class="title">Heat</span>
                    <span class="value" style="width: 20px;"></span>
                </div>
            </div>
        </div>
        <div class="col-xs-3">
            <div class="graph-opinions positive">
                <div class="opinion">
                    <span class="value" style="width: 100px;"></span>
                    <span class="title">Low</span>
                </div>
                <div class="opinion">
                    <span class="value" style="width: 70px;"></span>
                    <span class="title">Heat</span>
                </div>
                <div class="opinion">
                    <span class="value" style="width: 60px;"></span>
                    <span class="title">Heat</span>
                </div>
                <div class="opinion">
                    <span class="value" style="width: 60px;"></span>
                    <span class="title">Heat</span>
                </div>
                <div class="opinion">
                    <span class="value" style="width: 40px;"></span>
                    <span class="title">Heat</span>
                </div>
                <div class="opinion">
                    <span class="value" style="width: 10px;"></span>
                    <span class="title">Heat</span>
                </div>
            </div>
        </div>

        <div class="col-xs-6">
            <ul class="bxslider">
                <li>
                    <div class="app-box">
                        <dl>
                            <dt><img src="https://lh5.ggpht.com/0VYAvZLR9YhosF-thqm8xl8EWsCfrEY_uk2og2f59K8IOx5TfPsXjFVwxaHVnUbuEjc=w300" alt="" /></dt>
                            <dd>Messenger</dd>
                        </dl>
                    </div>
                </li>
                <li>
                    <div class="app-box">
                        <dl>
                            <dt><img src="https://lh5.ggpht.com/1CxNUEdzrREikWZoaHIU5J63x2gOxTb7R-ZIbJd51uPBFt0jUj8AX2bMOhKiIBcuAqtH=w300" alt="" /></dt>
                            <dd>Skype</dd>
                        </dl>
                    </div>
                </li>
                <li>
                    <div class="app-box">
                        <dl>
                            <dt><img src="https://lh5.ggpht.com/jWKeAZUzPtUqgZdcLDSa7vd3iwdan8QX4nlYlHBMkJHcxwr-uZU40vDmrMKczCmiCBRf=w300" alt="" /></dt>
                            <dd>Viber</dd>
                        </dl>
                    </div>
                </li>
                <li>
                    <div class="app-box">
                        <dl>
                            <dt><img src="https://lh6.ggpht.com/sRWS6JpaUYQPxdSPv7BLUWOsA9L7IRcvQOjT6GD5x2QMsaM5N2Glk88o7BjA2tWEMaRG=w300" alt="" /></dt>
                            <dd>Kik</dd>
                        </dl>
                    </div>
                </li>
            </ul>
        </div>
    </div>

    <script type="text/javascript">
        $(document).ready(function() """),format.raw/*133.38*/("""{"""),format.raw/*133.39*/("""
            """),format.raw/*134.13*/("""$(".bxslider").bxSlider("""),format.raw/*134.37*/("""{"""),format.raw/*134.38*/("""
                """),format.raw/*135.17*/("""slideMargin: 20,
                slideWidth: 200,
                maxSlides: 5,
                moveSlides: 1,
                pager: false
            """),format.raw/*140.13*/("""}"""),format.raw/*140.14*/(""");

            var opts = """),format.raw/*142.24*/("""{"""),format.raw/*142.25*/("""
                """),format.raw/*143.17*/("""lines: 12,
                angle: 0,
                lineWidth: 0.15,
                pointer: """),format.raw/*146.26*/("""{"""),format.raw/*146.27*/("""
                    """),format.raw/*147.21*/("""length: 0.4,
                    strokeWidth: 0.05,
                    color: '#000000'
                """),format.raw/*150.17*/("""}"""),format.raw/*150.18*/(""",
                limitMax: 'false',
                percentColors: [[0.0, "#cf1414" ], [0.5, "#aeaeae"], [1.0, "#1c8e17"]],
                strokeColor: '#e0e0e0',
                generateGradient: true
            """),format.raw/*155.13*/("""}"""),format.raw/*155.14*/(""";

            $(".gauge").each(function() """),format.raw/*157.41*/("""{"""),format.raw/*157.42*/("""
                """),format.raw/*158.17*/("""var gauge = new Gauge($(this).get(0)).setOptions(opts);
                gauge.maxValue = 100;
                gauge.animationSpeed = 300;
                gauge.set(parseInt($(this).data("value")));
            """),format.raw/*162.13*/("""}"""),format.raw/*162.14*/(""");
        """),format.raw/*163.9*/("""}"""),format.raw/*163.10*/(""")
    </script>
""")))}))
      }
    }
  }

  def render(id:String): play.twirl.api.HtmlFormat.Appendable = apply(id)

  def f:((String) => play.twirl.api.HtmlFormat.Appendable) = (id) => apply(id)

  def ref: this.type = this

}


}

/**/
object details extends details_Scope0.details
              /*
                  -- GENERATED --
                  DATE: Thu Jun 25 10:17:54 CEST 2015
                  SOURCE: /home/gaylor/Developpement/ObViz/obviz-app/app/views/details.scala.html
                  HASH: 89b2dee120d318bcc3be9219434b95e7a23c5cea
                  MATRIX: 749->1|856->13|884->16|897->22|936->24|967->29|6195->5228|6225->5229|6267->5242|6320->5266|6350->5267|6396->5284|6577->5436|6607->5437|6663->5464|6693->5465|6739->5482|6863->5577|6893->5578|6943->5599|7077->5704|7107->5705|7352->5921|7382->5922|7454->5965|7484->5966|7530->5983|7769->6193|7799->6194|7838->6205|7868->6206
                  LINES: 27->1|32->1|34->3|34->3|34->3|35->4|164->133|164->133|165->134|165->134|165->134|166->135|171->140|171->140|173->142|173->142|174->143|177->146|177->146|178->147|181->150|181->150|186->155|186->155|188->157|188->157|189->158|193->162|193->162|194->163|194->163
                  -- GENERATED --
              */
          