## Bot to scrape NLX listings from careeronestop.org
The National Listing eXchange (NLX) has an API, but I think you have to be a non-profit before they give you credentials.
The bot will take the following information from the web and outputs it to a csv file:
- Job Title
- Company Name
- Location 
- Date Posted 
- Link to the job description 
- And a count of the number of customizable keywords that were present in the posting

# To configure a run, edit the properties file located in `~/src/main/properties/run_settings.properties`
In order, the run parameters include:
- Which browser to use (out of Chrome, Firefox, and Edge)
- The job title and/or description you're searching for
- The location you're looking at (in the format "City Name, St")
- The name of the file to write to
- The date of the oldest listings you want to include (in the format MM/dd/yyyy)
- And a list of Strings that you hope to find in these listings (keywords seperated by commas)

# To run: execute the Main class as a java program
And then check `~/target` for your output.