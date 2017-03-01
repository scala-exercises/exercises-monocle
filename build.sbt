lazy val monocle = (project in file("."))
    .enablePlugins(ExerciseCompilerPlugin)
    .settings(
      organization := "org.scala-exercises",
      name         := "exercises-monocle",
      scalaVersion := "2.11.8",
      version := "0.3.0-SNAPSHOT",
      resolvers ++= Seq(
        Resolver.sonatypeRepo("snapshots"),
        Resolver.sonatypeRepo("releases")
      ),
      libraryDependencies ++= {
        val monocleVersion = "1.4.0"
        Seq(
          "com.github.julien-truffaut"  %%  "monocle-core" % monocleVersion,
          "com.github.julien-truffaut"  %%  "monocle-macro" % monocleVersion,
          "org.scalatest" %% "scalatest" % "2.2.6" exclude("org.scalacheck", "scalacheck"),
          "org.scala-exercises" %% "exercise-compiler" % version.value,
          "org.scala-exercises" %% "definitions" % version.value,
          "org.scalacheck" %% "scalacheck" % "1.12.5",
          "com.github.alexarchambault" %% "scalacheck-shapeless_1.12" % "0.3.1",
          compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.0")
        )
      }
    )
