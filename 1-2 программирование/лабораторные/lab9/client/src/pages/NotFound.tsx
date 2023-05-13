import * as React from 'react'
import { Typography } from '@mui/material'
import { Link } from 'react-router-dom'
import { Trans, useTranslation } from 'react-i18next'

const NotFound = () => {
  const { t } = useTranslation()

  return (
    <div className={classes.root}>
      <Typography className={classes.title}>four-o-four</Typography>
      <Typography
        className={classes.text}
      >{t`pages.NotFound.title`}</Typography>
      <Typography className={classes.text}>
        <Trans i18nKey="pages.NotFound.text">
          <Link to="/" className={classes.link} />
        </Trans>
      </Typography>
      <NotFoundSVG theme={theme.palette.type} className={classes.svg} />
    </div>
  )
}

export default React.memo(NotFound)
