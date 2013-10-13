Social Network Analytics Dashboard
==================================



## What is SNA ?

Social network analysis (SNA) is the methodical analysis of social networks. Social network analysis views social relationships in terms of network theory, consisting of nodes (representing individual actors within the network) and ties (which represent relationships between the individuals, such as friendship, kinship, organizational position, sexual relationships, etc.) These networks are often depicted in a social network diagram, where nodes are represented as points and ties are represented as lines

## Data Generation

The data is being generated from a hypothetical social media website. This hypothetical website has 2,500 users with approximately 65,000 edges between them. They come from interesting places such as Heaven and Hell and Asgard! And they don't sleep or eat or drink, they only gossip with each other on a wide variety of topics.

## Features

* Timestamped analysis of top 5 popular topics
* Trend analysis of top popular topics on the basis of number of users and volume
* Automatic downloader that schedules downloading of log files and population of databases
* Model architecture of database to have time efficient access of particulars of database
* A hierarchical filter that stores the volume of communication each day for the last 7 days, the communication each week for the last 4 weeks.


## Development

* Django is used as web framework. Django is written in python and follows the model–view–controller architectural pattern.
* Automatic schedular cron.py downloads the remote log files on local system.
* Python scripts are run over log files present on system to generate models.
* Models are used to make .tsv and .json format static files that are being hosted over localhost/static.
* These static files are fed into d3.js script to obtain trending timestamped charts.
