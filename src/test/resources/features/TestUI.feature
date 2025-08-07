@UI
Feature: Webpage actions

  Scenario: Open webpage and perform actions
    Given I open the webpage "https://ultimateqa.com/simple-html-elements-for-automation/"
    Then Capitalize the first letter of each word in the table title, excluding prepositions
    And Ensure all roles have salaries ≥ $100k and exclude Manual roles

  Scenario Outline: "Email me!" for 10 accounts
    Given I open the webpage "https://ultimateqa.com/simple-html-elements-for-automation/"
    Then I perform the Email me action with "<name>" and "<email>"

    Examples:
      | name   | email              |
      | kien1  | kitodo1@gmail.com  |
#      | kien2  | kitodo2@gmail.com  |
#      | kien3  | kitodo3@gmail.com  |
#      | kien4  | kitodo4@gmail.com  |
#      | kien5  | kitodo5@gmail.com  |
#      | kien6  | kitodo6@gmail.com  |
#      | kien7  | kitodo7@gmail.com  |
#      | kien8  | kitodo8@gmail.com  |
#      | kien9  | kitodo9@gmail.com  |
#      | kien10 | kitodo10@gmail.com |