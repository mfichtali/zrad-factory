package ma.zrad.system.vp.batch.domain.port.in;

import ma.zrad.system.batch.common.enums.SourceFileImporterEnum;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.vp.batch.domain.model.FileImporterMetricsDomain;

import java.nio.file.Path;

public interface FileImporterService {

    /**
     * Récupère un fichier depuis une source externe, le dépose dans le répertoire "input"
     * du contexte batch, et retourne le chemin du fichier prêt à traiter.
     *
     * @param context Contexte du batch contenant les répertoires de travail
     * @return Chemin du fichier copié dans "input"
     * @throws Exception En cas de problème
     */
    Path importFile(BatchFileContext fileCtx) throws Exception;

    /**
     * Nettoie les ressources après l'importation du fichier.
     *
     * @param fileCtx        Contexte du fichier batch
     * @param fileMetricData Données de métriques du fichier importé
     */
    void postImportCleanup(BatchFileContext fileCtx, FileImporterMetricsDomain fileMetricData);

    /**
     * Retourne l'énumération de la source de fichier importée.
     *
     * @return SourceFileImporterEnum représentant la source du fichier
     */
    SourceFileImporterEnum sourceImportFile();
}
