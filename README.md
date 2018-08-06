
[![Release](https://jitpack.io/v/mgsx-dev/gdx-analytics.svg)](https://jitpack.io/#mgsx-dev/gdx-analytics)

[Full documentation](https://jitpack.io/com/github/mgsx-dev/gdx-analytics/master-SNAPSHOT/javadoc/index.html)

User tracking for your LibGDX game.

| Compatibility    | Desktop      | WebGL        | Android      | iOS          |
|------------------|:------------:|:------------:|:------------:|:------------:|
| Google Analytic  | OK           | OK           | *not tested* | *not tested* |
| Matomo (Piwik)   | *not tested* | *not tested* | *not tested* | *not tested* |


# Setup

## Google Analytics

You need to create a new website to track and get its tracker ID.

see [Example here](test/net/mgsx/analytics/GoogleAnalyticsTrackerTest.java)

## Matomo (piwik)

see [Example here](test/net/mgsx/analytics/MatomoTrackerTest.java)

### Test with a local instance (docker)

run docker compose :

	$ cd docker/matomo
	$ docker-compose up web

Install : go to http://localhost:9000

Setup DB

* db address : db
* login: root
* pwd : root
* db name : piwik

Setup User

* login : matomo
* pwd : matomo
* email : your email

Setup website

* name : localhost
* geo : anything

Then run integration test to send an event :

* idsite must be your created site id (1 for the first one)

you can see the result [http://localhost:9000/index.php?module=CoreHome&action=index&date=today&period=day&idSite=1#?idSite=1&period=day&date=today&category=General_Actions&subcategory=Events_Events](here)
