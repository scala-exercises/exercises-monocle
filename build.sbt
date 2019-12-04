import ProjectPlugin.autoImport._

val scalaExercisesV = "0.5.0-SNAPSHOT"

def dep(artifactId: String) = "org.scala-exercises" %% artifactId % scalaExercisesV

lazy val monocle = (project in file("."))
  .enablePlugins(ExerciseCompilerPlugin)
  .settings(
    name := "exercises-monocle",
    libraryDependencies ++= Seq(
      dep("exercise-compiler"),
      dep("definitions"),
      %%("monocle-core", V.monocle),
      %%("monocle-macro", V.monocle),
      %%("scalatest", V.scalatest),
      %%("scalacheck", V.scalacheck),
      "org.typelevel"              %% "alleycats-core"            % V.cats,
      "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % V.scalacheckShapeless,
      "org.scalatestplus"          %% "scalatestplus-scalacheck"  % V.scalatestplusScheck
    ),
    addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.1" cross CrossVersion.full)
  )

// Distribution

pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")
