@order
Feature: Smoke test scenarios

  Background:
    Given address in city "Lviv" on street "Grushevskogo" "25"
    And system create hotel with name "Storium"
    Given price "125" dollars per day for room with type "3-bedroom"
    Given system create user with email "email@mail.com" and name "Ivan"


  @order-reject @order-accept
  Scenario Outline:  user book room, retrieve response with positive and negative status
    And user "<user-name>" has card number "<card-number>" with balance of "<balance>" dollars
    And "<user-name>" try to book room from "<start-date>" to "<end-date>"
    Then order has status "<order-status>"
    Examples:
      | user-name | card-number | balance | start-date    | end-date      | order-status |
      | Ivan      | 1234657890  | 250     | 1687651200000 | 1687996800000 | REJECT       |
      | Ivan      | 1234657891  | 2500    | 1690243200000 | 1690588800000 | ACCEPT       |

  @reject-due-dates
  Scenario Outline: user successfully booked room,user can`t book room for the same date
    And user "<user-name>" has card number "<card-number>" with balance of "<balance>" dollars
    And "<user-name>" try to book room from "<start-date>" to "<end-date>"
    Then order has status "<order-status>"
    And "<user-name>" try to book room from "<start-reject-date>" to "<end-reject-date>" dates again
    Examples:
      | user-name | card-number | balance | start-date    | end-date      | order-status | start-reject-date | end-reject-date |
      | Ivan      | 1234657892  | 5000    | 1719878400000 | 1720742400000 | ACCEPT       | 1719792000000     | 1720137600000   |
      | Ivan      | 1234657893  | 5000    | 1719878400000 | 1720742400000 | ACCEPT       | 1720051200000     | 1720310400000   |
      | Ivan      | 1234657894  | 5000    | 1719878400000 | 1720742400000 | ACCEPT       | 1720569600000     | 1721174400000   |

  @check-reservation-clearing
  Scenario Outline: user order rejected, reservation should be deleted too
    And user "<user-name>" has card number "<user-card>" with balance of "<card-balance>" dollars
    When "<user-name>" try to book room from "<start-date>" to "<end-date>"
    But  order has status "REJECT"
    When "<user-name>" replenished his card balance of "<replenish-price>" dollars
    Then "<user-name>" try to book room from "<start-date>" to "<end-date>" dates again
    Then expect order has status become "ACCEPT"
    Examples:
      | user-name | user-card  | card-balance | start-date    | end-date      | replenish-price |
      | Ivan      | 1234657895 | 100          | 1687438560000 | 1687697760000 | 5000            |
