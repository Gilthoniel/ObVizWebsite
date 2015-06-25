
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object main_Scope0 {
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

class main extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template1[Html,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/()(content: Html):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.19*/("""

"""),format.raw/*3.1*/("""<!DOCTYPE html>

<html lang="en">
    <head>
        <title>ObViz</title>

        <meta charset="utf-8" />
        <meta name="description" content="Read opinions in app reviews and get the app you want" />
        <meta name="author" content="ObViz" />

        """),format.raw/*13.22*/("""
        """),format.raw/*14.9*/("""<link rel="shortcut icon" type="image/png" href=""""),_display_(/*14.59*/routes/*14.65*/.Assets.versioned("images/favicon.png")),format.raw/*14.104*/("""">

        """),format.raw/*16.26*/("""
        """),format.raw/*17.9*/("""<link rel="stylesheet" media="screen" href=""""),_display_(/*17.54*/routes/*17.60*/.Assets.versioned("stylesheets/design.css")),format.raw/*17.103*/("""">
        <link rel="stylesheet" media="screen" href=""""),_display_(/*18.54*/routes/*18.60*/.Assets.versioned("bootstrap/less/bootstrap.css")),format.raw/*18.109*/("""">
        <link rel="stylesheet" media="screen" href=""""),_display_(/*19.54*/routes/*19.60*/.Assets.versioned("stylesheets/jquery.bxslider.css")),format.raw/*19.112*/("""">

        """),format.raw/*21.35*/("""
        """),format.raw/*22.9*/("""<script type="text/javascript" src=""""),_display_(/*22.46*/routes/*22.52*/.Assets.versioned("lib/jquery/jquery.min.js")),format.raw/*22.97*/(""""></script>
        <script type="text/javascript" src=""""),_display_(/*23.46*/routes/*23.52*/.Assets.versioned("lib/bootstrap/js/bootstrap.min.js")),format.raw/*23.106*/(""""></script>
        <script type="text/javascript" src=""""),_display_(/*24.46*/routes/*24.52*/.Assets.versioned("javascripts/jquery.bxslider.min.js")),format.raw/*24.107*/(""""></script>
        <script type="text/javascript" src=""""),_display_(/*25.46*/routes/*25.52*/.Assets.versioned("javascripts/gauge.min.js")),format.raw/*25.97*/(""""></script>

        <!--[if lt IE 9]>
            <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

    </head>
    <body>
        <nav class="navbar navbar-inverse navbar-static-top">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#obviz-navbar" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href=""""),_display_(/*42.52*/routes/*42.58*/.Application.index()),format.raw/*42.78*/("""">
                        <img height="20" alt="ObViz" src=""""),_display_(/*43.60*/routes/*43.66*/.Assets.versioned("images/favicon.png")),format.raw/*43.105*/("""">
                    </a>
                </div>

                <div class="collapse navbar-collapse" id="obviz-navbar">
                    <ul class="nav navbar-nav">
                        <li><a href=""""),_display_(/*49.39*/routes/*49.45*/.Application.index()),format.raw/*49.65*/("""">Home</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class="container">
            """),_display_(/*56.14*/content),format.raw/*56.21*/("""
        """),format.raw/*57.9*/("""</div>
    </body>
</html>
"""))
      }
    }
  }

  def render(content:Html): play.twirl.api.HtmlFormat.Appendable = apply()(content)

  def f:(() => (Html) => play.twirl.api.HtmlFormat.Appendable) = () => (content) => apply()(content)

  def ref: this.type = this

}


}

/**/
object main extends main_Scope0.main
              /*
                  -- GENERATED --
                  DATE: Wed Jun 24 15:45:59 CEST 2015
                  SOURCE: /home/gaylor/Developpement/ObViz/obviz-app/app/views/main.scala.html
                  HASH: f43a98c1500013d8efee6b132655579a58614910
                  MATRIX: 741->1|853->18|881->20|1173->297|1209->306|1286->356|1301->362|1362->401|1402->430|1438->439|1510->484|1525->490|1590->533|1673->589|1688->595|1759->644|1842->700|1857->706|1931->758|1971->796|2007->805|2071->842|2086->848|2152->893|2236->950|2251->956|2327->1010|2411->1067|2426->1073|2503->1128|2587->1185|2602->1191|2668->1236|3473->2014|3488->2020|3529->2040|3618->2102|3633->2108|3694->2147|3932->2358|3947->2364|3988->2384|4160->2529|4188->2536|4224->2545
                  LINES: 27->1|32->1|34->3|44->13|45->14|45->14|45->14|45->14|47->16|48->17|48->17|48->17|48->17|49->18|49->18|49->18|50->19|50->19|50->19|52->21|53->22|53->22|53->22|53->22|54->23|54->23|54->23|55->24|55->24|55->24|56->25|56->25|56->25|73->42|73->42|73->42|74->43|74->43|74->43|80->49|80->49|80->49|87->56|87->56|88->57
                  -- GENERATED --
              */
          