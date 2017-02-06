package edu.knoldus

import org.apache.log4j.Logger
import org.scalatest.FunSuite
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class TweetTester extends FunSuite {

  val logger = Logger.getLogger(this.getClass)

  val t = new Tweet
  test("Test case to check for the total no. of tweets") {
    val noOfTweets = t.getNoOfTweets()
    val expectedTweets = Await.result(noOfTweets, Duration.Inf)
    assert(expectedTweets == 100)
  }

  test("Test case to check for the retweets and likes") {
    val favouriteList = t.getLikesAndRetweets()
    val expectedList = Await.result(favouriteList, Duration.Inf)
    assert(expectedList.size == 100)
  }

}
