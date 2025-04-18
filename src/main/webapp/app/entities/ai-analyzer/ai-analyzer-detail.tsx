import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ai-analyzer.reducer';

export const AIAnalyzerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const aIAnalyzerEntity = useAppSelector(state => state.aIAnalyzer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aIAnalyzerDetailsHeading">
          <Translate contentKey="smartVaxApp.aIAnalyzer.detail.title">AIAnalyzer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aIAnalyzerEntity.id}</dd>
          <dt>
            <span id="analysisResult">
              <Translate contentKey="smartVaxApp.aIAnalyzer.analysisResult">Analysis Result</Translate>
            </span>
          </dt>
          <dd>{aIAnalyzerEntity.analysisResult}</dd>
          <dt>
            <Translate contentKey="smartVaxApp.aIAnalyzer.feedback">Feedback</Translate>
          </dt>
          <dd>{aIAnalyzerEntity.feedback ? aIAnalyzerEntity.feedback.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ai-analyzer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ai-analyzer/${aIAnalyzerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AIAnalyzerDetail;
