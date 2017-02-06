package edu.knoldus

import java.io.File
import com.typesafe.config.{ConfigException, ConfigFactory}
import org.apache.log4j.Logger
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Twitter, TwitterFactory}

/*
* A singleton object to fetch the secret keys and tokens from the conf file using typesafe config api.
* */

object Config {

  val parsedConfig = ConfigFactory.parseFile(new File(".\\src\\main\\resources\\application.conf"))
  val conf = ConfigFactory.load(parsedConfig)
  val logger = Logger.getLogger(this.getClass)

  /*
  * @ returns an Option type by setting up and fetching data from the config file
  * if option is Some(twitter) or None
  * */
  def getKeysAndTokens(): Option[Twitter] = {
    try {
      val consumerKey = conf.getString("consumerKey")
      val consumerSecretKey = conf.getString("consumerSecretKey")
      val accessToken = conf.getString("accessToken")
      val accessTokenSecret = conf.getString("accessTokenSecret")
      val confBuilder = new ConfigurationBuilder()
      confBuilder.setDebugEnabled(false)
        .setOAuthConsumerKey(consumerKey)
        .setOAuthConsumerSecret(consumerSecretKey)
        .setOAuthAccessToken(accessToken)
        .setOAuthAccessTokenSecret(accessTokenSecret)
      val twitter: Twitter = new TwitterFactory(confBuilder.build()).getInstance()
      Some(twitter)
    }
    catch {
      case ex: ConfigException => logger.warn("Exception Occurred! " + ex.getMessage)
        None
    }
  }

}
