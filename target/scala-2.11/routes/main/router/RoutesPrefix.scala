
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/gaylor/Developpement/ObViz/app-site/conf/routes
// @DATE:Thu Jun 25 10:53:34 CEST 2015


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
