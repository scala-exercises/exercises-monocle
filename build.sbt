import com.jsuereth.sbtpgp.PgpKeys.publishSigned

ThisBuild / organization       := "org.scala-exercises"
ThisBuild / githubOrganization := "47degrees"
ThisBuild / scalaVersion       := "2.13.8"

// Required to prevent errors for eviction from binary incompatible dependency
// resolutions.
// See also: https://github.com/scala-exercises/exercises-cats/pull/267
ThisBuild / libraryDependencySchemes += "org.scala-lang.modules" %% "scala-xml" % "always"

// This is required by the exercises compiler:
publishLocal  := (publishLocal dependsOn compile).value
publishSigned := (publishSigned dependsOn compile).value

addCommandAlias("ci-test", "scalafmtCheckAll; scalafmtSbtCheck; test")
addCommandAlias("ci-docs", "github; documentation/mdoc; headerCreateAll")
addCommandAlias("ci-publish", "github; ci-release")

lazy val exercises = (project in file("."))
  .settings(name := "exercises-monocle")
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-exercises"        %% "exercise-compiler"         % "0.7.1",
      "org.scala-exercises"        %% "definitions"               % "0.7.1",
      "org.typelevel"              %% "alleycats-core"            % "2.7.0",
      "org.typelevel"              %% "cats-core"                 % "2.7.0",
      "com.github.julien-truffaut" %% "monocle-core"              % "2.1.0",
      "com.github.julien-truffaut" %% "monocle-macro"             % "2.1.0",
      "com.chuusai"                %% "shapeless"                 % "2.3.9",
      "org.scalatest"              %% "scalatest"                 % "3.2.11",
      "org.scalacheck"             %% "scalacheck"                % "1.15.4",
      "org.scalatestplus"          %% "scalacheck-1-15"           % "3.2.9.0",
      "com.github.alexarchambault" %% "scalacheck-shapeless_1.15" % "1.3.0"
    ),
    scalacOptions += "-Ymacro-annotations",
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full)
  )
  .enablePlugins(ExerciseCompilerPlugin)

lazy val documentation = project
  .settings(mdocOut := file("."))
  .settings(publish / skip := true)
  .enablePlugins(MdocPlugin)
