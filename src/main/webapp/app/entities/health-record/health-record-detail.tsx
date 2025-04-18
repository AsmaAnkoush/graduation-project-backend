import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './health-record.reducer';

export const HealthRecordDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const healthRecordEntity = useAppSelector(state => state.healthRecord.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="healthRecordDetailsHeading">
          <Translate contentKey="smartVaxApp.healthRecord.detail.title">HealthRecord</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{healthRecordEntity.id}</dd>
          <dt>
            <span id="sensitivity">
              <Translate contentKey="smartVaxApp.healthRecord.sensitivity">Sensitivity</Translate>
            </span>
          </dt>
          <dd>{healthRecordEntity.sensitivity}</dd>
          <dt>
            <span id="diabetes">
              <Translate contentKey="smartVaxApp.healthRecord.diabetes">Diabetes</Translate>
            </span>
          </dt>
          <dd>{healthRecordEntity.diabetes ? 'true' : 'false'}</dd>
          <dt>
            <span id="highBloodPressure">
              <Translate contentKey="smartVaxApp.healthRecord.highBloodPressure">High Blood Pressure</Translate>
            </span>
          </dt>
          <dd>{healthRecordEntity.highBloodPressure ? 'true' : 'false'}</dd>
          <dt>
            <span id="geneticDiseases">
              <Translate contentKey="smartVaxApp.healthRecord.geneticDiseases">Genetic Diseases</Translate>
            </span>
          </dt>
          <dd>{healthRecordEntity.geneticDiseases}</dd>
          <dt>
            <span id="bloodType">
              <Translate contentKey="smartVaxApp.healthRecord.bloodType">Blood Type</Translate>
            </span>
          </dt>
          <dd>{healthRecordEntity.bloodType}</dd>
        </dl>
        <Button tag={Link} to="/health-record" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/health-record/${healthRecordEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default HealthRecordDetail;
