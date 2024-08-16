# language: en

  Feature: List cuisines

  As an API client
  I want to get a list of cuisines
  To see all the cuisines registered in the system

  Scenario: List all the cuisines successfully
    Given that there are cuisines registered in the system:
      | name | Brazilian |
      | name | German    |
      | name | Italian   |
      | name | Fast Food |
    When i make a GET request to retrieve the cuisines
    Then the response must have the status code 200
    And the response must contain the data that was recorded earlier
