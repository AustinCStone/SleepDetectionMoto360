package wirelessnetworks.remsleepdetection;

/**
 * Created by austinstone on 10/18/14.
 */
public class AccelerometerData {
    public double[] x;
    public double[] y;
    public double[] z;
    public int dataLength;

    public AccelerometerData(int length) {
        this.x = new double[length];
        this.y = new double[length];
        this.z = new double[length];
        this.dataLength = length;
    }

    public double classify() {


        double totalSum = 0;
        double expectedSumStill = 9.8;
        double differenceFromExpectedSumStill = 0.0;
        for (int i = 0; i < this.dataLength; i++) {

            totalSum = Math.sqrt((this.x[i])*(this.x[i]) + (this.y[i])*(this.y[i]) + (this.z[i])*(this.z[i]));
            differenceFromExpectedSumStill += Math.abs(totalSum - expectedSumStill);
        }
        return differenceFromExpectedSumStill/(double)this.dataLength;
    }
}
