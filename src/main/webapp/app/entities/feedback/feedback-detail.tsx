import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './feedback.reducer';

export const FeedbackDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const feedbackEntity = useAppSelector(state => state.feedback.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="feedbackDetailsHeading">
          <Translate contentKey="smartVaxApp.feedback.detail.title">Feedback</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.id}</dd>
          <dt>
            <span id="messageText">
              <Translate contentKey="smartVaxApp.feedback.messageText">Message Text</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.messageText}</dd>
          <dt>
            <span id="sideEffects">
              <Translate contentKey="smartVaxApp.feedback.sideEffects">Side Effects</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.sideEffects}</dd>
          <dt>
            <span id="treatment">
              <Translate contentKey="smartVaxApp.feedback.treatment">Treatment</Translate>
            </span>
          </dt>
          <dd>{feedbackEntity.treatment}</dd>
          <dt>
            <span id="dateSubmitted">
              <Translate contentKey="smartVaxApp.feedback.dateSubmitted">Date Submitted</Translate>
            </span>
          </dt>
          <dd>
            {feedbackEntity.dateSubmitted ? <TextFormat value={feedbackEntity.dateSubmitted} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="smartVaxApp.feedback.parent">Parent</Translate>
          </dt>
          <dd>{feedbackEntity.parent ? feedbackEntity.parent.id : ''}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.feedback.vaccination">Vaccination</Translate>
          </dt>
          <dd>{feedbackEntity.vaccination ? feedbackEntity.vaccination.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/feedback" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/feedback/${feedbackEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FeedbackDetail;
