import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/parent">
        <Translate contentKey="global.menu.entities.parent" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/health-record">
        <Translate contentKey="global.menu.entities.healthRecord" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/child">
        <Translate contentKey="global.menu.entities.child" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vaccination">
        <Translate contentKey="global.menu.entities.vaccination" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/schedule-vaccination">
        <Translate contentKey="global.menu.entities.scheduleVaccination" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/health-worker">
        <Translate contentKey="global.menu.entities.healthWorker" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/appointment">
        <Translate contentKey="global.menu.entities.appointment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/feedback">
        <Translate contentKey="global.menu.entities.feedback" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ai-analyzer">
        <Translate contentKey="global.menu.entities.aiAnalyzer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reminder">
        <Translate contentKey="global.menu.entities.reminder" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
