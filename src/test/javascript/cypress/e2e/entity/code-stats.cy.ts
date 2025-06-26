import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('CodeStats e2e test', () => {
  const codeStatsPageUrl = '/code-stats';
  const codeStatsPageUrlPattern = new RegExp('/code-stats(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const codeStatsSample = {};

  let codeStats;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/code-stats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/code-stats').as('postEntityRequest');
    cy.intercept('DELETE', '/api/code-stats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (codeStats) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/code-stats/${codeStats.id}`,
      }).then(() => {
        codeStats = undefined;
      });
    }
  });

  it('CodeStats menu should load CodeStats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('code-stats');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CodeStats').should('exist');
    cy.url().should('match', codeStatsPageUrlPattern);
  });

  describe('CodeStats page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(codeStatsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CodeStats page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/code-stats/new$'));
        cy.getEntityCreateUpdateHeading('CodeStats');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', codeStatsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/code-stats',
          body: codeStatsSample,
        }).then(({ body }) => {
          codeStats = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/code-stats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [codeStats],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(codeStatsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CodeStats page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('codeStats');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', codeStatsPageUrlPattern);
      });

      it('edit button click should load edit CodeStats page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CodeStats');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', codeStatsPageUrlPattern);
      });

      it('edit button click should load edit CodeStats page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CodeStats');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', codeStatsPageUrlPattern);
      });

      it('last delete button click should delete instance of CodeStats', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('codeStats').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', codeStatsPageUrlPattern);

        codeStats = undefined;
      });
    });
  });

  describe('new CodeStats page', () => {
    beforeEach(() => {
      cy.visit(`${codeStatsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CodeStats');
    });

    it('should create an instance of CodeStats', () => {
      cy.get(`[data-cy="instructions"]`).type('1589.24');
      cy.get(`[data-cy="instructions"]`).should('have.value', '1589.24');

      cy.get(`[data-cy="branches"]`).type('2747.93');
      cy.get(`[data-cy="branches"]`).should('have.value', '2747.93');

      cy.get(`[data-cy="lines"]`).type('5503.1');
      cy.get(`[data-cy="lines"]`).should('have.value', '5503.1');

      cy.get(`[data-cy="methods"]`).type('24950.51');
      cy.get(`[data-cy="methods"]`).should('have.value', '24950.51');

      cy.get(`[data-cy="classes"]`).type('25568.97');
      cy.get(`[data-cy="classes"]`).should('have.value', '25568.97');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        codeStats = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', codeStatsPageUrlPattern);
    });
  });
});
