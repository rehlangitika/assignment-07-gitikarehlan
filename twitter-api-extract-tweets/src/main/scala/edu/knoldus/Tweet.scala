package edu.knoldus

import java.util.Date
import org.apache.log4j.Logger
import twitter4j._
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/*
* this class has methods to fetch tweets, count the no. of tweets and calculating the no. of likes and retweet count.
* Has 2 case classes for generating and storing tweets (MyTweets), generating and storing likes and retweets (FavouriteCount)
* */
class Tweet {

  val logger = Logger.getLogger(this.getClass)

  /*
  * @ returns the total no of tweets i.e. an Int wrapped around Future.
  * */
  def getNoOfTweets(): Future[Int] = Future {
    val tweetList = getTweets
    tweetList.size
  }

  /*
  *
  * @fetches the tweets suing twitter4j api and returns a List of tweets*/
  def getTweets: List[MyTweets] = {
    val twitter = Config.getKeysAndTokens()
    twitter match {
      case Some(twitter) => {
        val query = new Query("#india")
        query.setCount(100)
        val list = twitter.search(query)
        val tweets = list.getTweets.asScala.toList
        val allTweets = tweets.map {
          tweet =>
            MyTweets(tweet.getText, tweet.getUser.getScreenName, tweet.getCreatedAt)
        }
        allTweets.sortBy(_.createdAt)
      }
      case None => {
        logger.warn("Configuration Failed!")
        List[MyTweets]()
      }
    }
  }

  /*
  * @returns a List of Favourite count (having likes and retweets counts) wrapped inside a future
  * */
  def getLikesAndRetweets(): Future[List[FavouriteCount]] = Future {
    val twitter = Config.getKeysAndTokens()
    twitter match {
      case Some(twitter) => {
        val query = new Query("#Trump")
        query.setCount(100)
        val list = twitter.search(query)
        val tweets = list.getTweets.asScala.toList
        val allTweets = tweets.map {
          tweet =>
            FavouriteCount(tweet.getRetweetCount, tweet.getFavoriteCount)
        }
        allTweets.sortBy(_.like)
      }
      case None => {
        logger.warn("Configuration Failed!")
        List[FavouriteCount]()
      }
    }
  }

  /*
  * @case class for generating and storing the Tweets fetched
  * */
  case class MyTweets(msg: String, userName: String, createdAt: Date)

  /*
  * @case class for generating a favourite count involving likes and retweets for the tweets
  * */
  case class FavouriteCount(retweet: Int, like: Int)

}
