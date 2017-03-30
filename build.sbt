val scalaExercicesV = "0.4.0-SNAPSHOT"

def dep(artifactId: String) = "org.scala-exercises" %% artifactId % scalaExercicesV

lazy val monocle = (project in file("."))
.enablePlugins(ExerciseCompilerPlugin)
.settings(
     name         := "exercises-monocle",
     libraryDependencies ++= Seq(
        dep("exercise-compiler"),
        dep("definitions"),
        %%("monocle-core" ),
        %%("monocle-macro" ),
        %%("scalatest"),
        %%("scalacheck"),
        %%("scheckShapeless")
    )
)

// Distribution

pgpPassphrase := Some(getEnvVar("PGP_PASSPHRASE").getOrElse("").toCharArray)
pgpPublicRing := file(s"$gpgFolder/pubring.gpg")
pgpSecretRing := file(s"$gpgFolder/secring.gpg")