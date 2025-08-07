@headless @Profile
Feature: Create Profile and Get Profile Data

  Scenario Outline: Validate API Create a new profile
    Given the user sends a invalid POST request to create a profile from feature
      """
      {
        "username": "<username>",
        "dateOfBirth": "<dateOfBirth>",
        "gender": "<gender>",
        "subscribedMarketing": <subscribedMarketing>
      }
      """
    When the user should receive a successful response with status "<status>" and message "<errorMessage>"
    Examples:
      | username   | dateOfBirth | gender | subscribedMarketing | status | errorMessage                              |
      |            | 1989-06-11  | MALE   | true                | 400    | Username is required                      |
      | kitodo1989 | 1989-06-11  | MALE   | true                | 400    | Username already exists                   |
      | kitodo1989 | 2025/08/08  | FEMALE | false               | 400    | Date of birth must be a valid date        |
      | kitodo1989 | 1989-06-11  | 1      | false               | 400    | Gender must be one of MALE, FEMALE, OTHER |

  Scenario Outline: Create a new profile and get the profile data
    Given the user sends a POST request to create a profile from feature
      """
      {
        "username": "<username>",
        "dateOfBirth": "<dateOfBirth>",
        "gender": "<gender>",
        "subscribedMarketing": <subscribedMarketing>
      }
      """
    When the user should receive a successful response with status "<status>"
    Then the user should receive a valid userId
    When the user sends a valid GET request to fetch the profile with the created userId
    Then the profile should contain the created userId
    And the profile should contain username correctly
    And the profile should contain dateOfBirth correctly
    And the profile should contain gender correctly
    And the profile should contain subscribedMarketing correctly
    And I connect database and I query data inserted correctly
    And I verify new profile is unique

    Examples:
      | username   | dateOfBirth | gender | subscribedMarketing | status |
      | ekinnguyen | 1989-06-11  | MALE   | true                | 201    |
      | kitodo     | 1985-12-15  | FEMALE | false               | 201    |
