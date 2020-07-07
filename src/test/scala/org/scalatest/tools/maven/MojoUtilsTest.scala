package org.scalatest.tools.maven

import com.github.stefanbirkner.systemlambda.SystemLambda._
import org.junit.contrib.java.lang.system.{ClearSystemProperties, EnvironmentVariables}
import org.junit.{Rule, Test}

class MojoUtilsTest {
  val _env: EnvironmentVariables = new EnvironmentVariables()
  val _sys: ClearSystemProperties = new ClearSystemProperties("java.home")

  @Rule
  def env: EnvironmentVariables = _env

  @Rule
  def sys: ClearSystemProperties = _sys

  @Test
  def getJvmHappyPath(): Unit = {
    withEnvironmentVariable("JAVA_HOME", null).execute(() -> {
      env.clear("JAVA_HOME")
      System.setProperty("java.home", "/test/jvm")
      assert(MojoUtils.getJvm == "/test/jvm/bin/java")
    })
  }

  @Test
  def getJvmWithoutJavaHome(): Unit = {
    withEnvironmentVariable("JAVA_HOME", null).execute(() -> {
      env.clear("JAVA_HOME")
      assert(MojoUtils.getJvm == "java")
    }
  }

  @Test
  def getJvmFromEnvironment(): Unit = {

    withEnvironmentVariable("JAVA_HOME", null).execute(() -> {
      env.clear("JAVA_HOME")
      env.set("JAVA_HOME", "/opt/jdk-11")
      Console.print(MojoUtils.getJvm)
      assert(MojoUtils.getJvm == "/opt/jdk-11/bin/java")
    }
  }

  @Test
  def getJvmJavaHomeIsPriority(): Unit = {
    System.setProperty("java.home", "/test/jvm")
    withEnvironmentVariable("JAVA_HOME", "/opt/jdk-11").execute(() -> {
      assert(MojoUtils.getJvm == "/opt/jdk-11/bin/java")
    }
  }
}
