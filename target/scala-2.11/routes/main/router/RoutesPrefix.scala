
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/gaylor/Developpement/ObViz/obviz-app/conf/routes
// @DATE:Thu Jun 25 08:15:53 CEST 2015


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
