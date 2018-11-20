package frc.robot.motionprofiles;

public class MotionProfileTests {

	public static class Test1 implements Path {
		@Override
		public double[][] mpL() {
			double[][] mpL = { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0.000244140625, 0 }, { 0, 0 },
					{ 0, 0 }, { 0, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0.000244140625, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
					{ 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
					{ 0, 0 }, { 0, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 },
					{ 0, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0.000244140625, 0 }, { 0, 0 },
					{ 0, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0.000244140625, 0 },
					{ 0.00048828125, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0.006591796875, 0 },
					{ 0.006591796875, 0 }, { 0.011474609375, 0.001708984375 }, { 0.016845703125, 0.0048828125 },
					{ 0.021484375, 0.009765625 }, { 0.030029296875, 0.0146484375 }, { 0.04248046875, 0.02001953125 },
					{ 0.056884765625, 0.025634765625 }, { 0.069091796875, 0.032470703125 },
					{ 0.0859375, 0.04052734375 }, { 0.102294921875, 0.05029296875 }, { 0.119384765625, 0.06005859375 },
					{ 0.135986328125, 0.069091796875 }, { 0.151611328125, 0.0751953125 },
					{ 0.166015625, 0.078369140625 }, { 0.1806640625, 0.0791015625 }, { 0.192138671875, 0.078369140625 },
					{ 0.210693359375, 0.07666015625 }, { 0.22314453125, 0.07470703125 }, { 0.23828125, 0.072998046875 },
					{ 0.25439453125, 0.072265625 }, { 0.27099609375, 0.072509765625 }, { 0.2900390625, 0.073974609375 },
					{ 0.328125, 0.07958984375 }, { 0.351806640625, 0.083984375 }, { 0.37841796875, 0.08984375 },
					{ 0.40625, 0.097412109375 }, { 0.43701171875, 0.105224609375 }, { 0.46875, 0.1142578125 },
					{ 0.501220703125, 0.123779296875 }, { 0.533935546875, 0.13330078125 },
					{ 0.560302734375, 0.141845703125 }, { 0.592529296875, 0.148681640625 },
					{ 0.6240234375, 0.15283203125 }, { 0.65380859375, 0.155029296875 }, { 0.6826171875, 0.1552734375 },
					{ 0.714111328125, 0.154052734375 }, { 0.7451171875, 0.15185546875 },
					{ 0.776123046875, 0.1494140625 }, { 0.807373046875, 0.1474609375 },
					{ 0.833251953125, 0.1474609375 }, { 0.866943359375, 0.148681640625 },
					{ 0.902099609375, 0.151123046875 }, { 0.937744140625, 0.15380859375 },
					{ 0.9755859375, 0.15771484375 }, { 1.013427734375, 0.162353515625 },
					{ 1.0498046875, 0.167724609375 }, { 1.087646484375, 0.172119140625 },
					{ 1.119873046875, 0.176025390625 }, { 1.16162109375, 0.178955078125 },
					{ 1.20458984375, 0.1826171875 }, { 1.25048828125, 0.186767578125 }, { 1.2978515625, 0.19287109375 },
					{ 1.347412109375, 0.2001953125 }, { 1.397705078125, 0.20947265625 },
					{ 1.448974609375, 0.218505859375 }, { 1.49853515625, 0.2275390625 },
					{ 1.538330078125, 0.233642578125 }, { 1.583740234375, 0.23779296875 },
					{ 1.628662109375, 0.2392578125 }, { 1.6728515625, 0.23828125 }, { 1.71728515625, 0.23486328125 },
					{ 1.762451171875, 0.2294921875 }, { 1.807861328125, 0.223876953125 },
					{ 1.845703125, 0.21923828125 }, { 1.892578125, 0.21728515625 }, { 1.940185546875, 0.2177734375 },
					{ 1.98779296875, 0.22021484375 }, { 2.034423828125, 0.222900390625 },
					{ 2.079833984375, 0.22509765625 }, { 2.12451171875, 0.22607421875 },
					{ 2.1689453125, 0.225830078125 }, { 2.213134765625, 0.224365234375 },
					{ 2.24951171875, 0.221923828125 }, { 2.29541015625, 0.218994140625 },
					{ 2.343017578125, 0.21728515625 }, { 2.3916015625, 0.217041015625 },
					{ 2.44091796875, 0.219482421875 }, { 2.49072265625, 0.22314453125 },
					{ 2.53857421875, 0.227783203125 }, { 2.58447265625, 0.231201171875 },
					{ 2.62939453125, 0.233154296875 }, { 2.673583984375, 0.23291015625 },
					{ 2.7080078125, 0.231201171875 }, { 2.751220703125, 0.2275390625 },
					{ 2.79443359375, 0.222412109375 }, { 2.837890625, 0.21728515625 },
					{ 2.881591796875, 0.213134765625 }, { 2.9248046875, 0.210205078125 },
					{ 2.958251953125, 0.208740234375 }, { 3.00634765625, 0.2080078125 },
					{ 3.037353515625, 0.20751953125 }, { 3.07373046875, 0.205810546875 },
					{ 3.108154296875, 0.20263671875 }, { 3.140380859375, 0.19677734375 },
					{ 3.1708984375, 0.180419921875 }, { 3.229736328125, 0.171142578125 },
					{ 3.2568359375, 0.162353515625 }, { 3.279052734375, 0.155029296875 }, { 3.307373046875, 0.1484375 },
					{ 3.335205078125, 0.14306640625 }, { 3.36328125, 0.13916015625 }, { 3.392578125, 0.136474609375 },
					{ 3.421630859375, 0.1357421875 }, { 3.451416015625, 0.1357421875 },
					{ 3.47412109375, 0.136962890625 }, { 3.507568359375, 0.137939453125 },
					{ 3.529052734375, 0.138671875 }, { 3.5556640625, 0.138671875 }, { 3.58056640625, 0.1376953125 },
					{ 3.604736328125, 0.135986328125 }, { 3.627685546875, 0.1328125 },
					{ 3.649169921875, 0.128662109375 }, { 3.666015625, 0.1240234375 },
					{ 3.686767578125, 0.118896484375 }, { 3.705810546875, 0.114013671875 },
					{ 3.723388671875, 0.109130859375 }, { 3.739013671875, 0.10400390625 },
					{ 3.7529296875, 0.098876953125 }, { 3.765869140625, 0.09326171875 }, { 3.77734375, 0.086669921875 },
					{ 3.788330078125, 0.07958984375 }, { 3.798828125, 0.072265625 }, { 3.8076171875, 0.06640625 },
					{ 3.818115234375, 0.06103515625 }, { 3.828125, 0.05712890625 }, { 3.837646484375, 0.054443359375 },
					{ 3.846435546875, 0.05224609375 }, { 3.854248046875, 0.050537109375 },
					{ 3.86181640625, 0.048583984375 }, { 3.8671875, 0.04638671875 }, { 3.873046875, 0.043701171875 },
					{ 3.876708984375, 0.04052734375 }, { 3.877685546875, 0.036865234375 },
					{ 3.876953125, 0.0322265625 }, { 3.876220703125, 0.025634765625 }, { 3.8759765625, 0.01904296875 },
					{ 3.87548828125, 0.011474609375 }, { 3.87548828125, 0.00537109375 }, { 3.875, 0.000732421875 },
					{ 3.87451171875, -0.0009765625 }, { 3.8740234375, -0.001708984375 },
					{ 3.874267578125, -0.001708984375 }, };
			return mpL;
		};

		@Override
		public double[][] mpR() {
			double[][] mpR = { { 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0.000244140625, 0 },
					{ 0, 0 }, { 0, 0 }, { 0, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0.000244140625, 0 },
					{ 0, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0.000244140625, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0.000244140625, 0 },
					{ 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0.000244140625, 0 },
					{ 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0.000244140625, 0 }, { 0.000244140625, 0 },
					{ 0.000244140625, 0 }, { 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 },
					{ 0.000244140625, 0 }, { 0.000244140625, 0 }, { -0.00048828125, 0 }, { -0.00048828125, 0 },
					{ -0.00439453125, 0 }, { -0.0107421875, -0.003173828125 }, { -0.01806640625, -0.006103515625 },
					{ -0.01806640625, -0.006103515625 }, { -0.026123046875, -0.010009765625 },
					{ -0.03759765625, -0.015625 }, { -0.05078125, -0.022705078125 }, { -0.0654296875, -0.03125 },
					{ -0.080322265625, -0.04052734375 }, { -0.0966796875, -0.050048828125 },
					{ -0.113037109375, -0.058837890625 }, { -0.123779296875, -0.065673828125 },
					{ -0.137451171875, -0.070068359375 }, { -0.152099609375, -0.072265625 },
					{ -0.16552734375, -0.072509765625 }, { -0.178955078125, -0.07177734375 },
					{ -0.192138671875, -0.070068359375 }, { -0.205078125, -0.068115234375 },
					{ -0.216552734375, -0.06640625 }, { -0.23193359375, -0.0654296875 },
					{ -0.249267578125, -0.065673828125 }, { -0.28662109375, -0.07421875 },
					{ -0.308837890625, -0.07421875 }, { -0.332763671875, -0.079833984375 },
					{ -0.35302734375, -0.0869140625 }, { -0.38037109375, -0.0947265625 },
					{ -0.407470703125, -0.10302734375 }, { -0.436279296875, -0.111328125 },
					{ -0.465576171875, -0.119140625 }, { -0.4951171875, -0.1259765625 },
					{ -0.525146484375, -0.13134765625 }, { -0.54833984375, -0.1357421875 },
					{ -0.605224609375, -0.14013671875 }, { -0.605224609375, -0.14013671875 },
					{ -0.633544921875, -0.140625 }, { -0.661865234375, -0.14013671875 },
					{ -0.69091796875, -0.13916015625 }, { -0.7197265625, -0.137939453125 },
					{ -0.7431640625, -0.13720703125 }, { -0.7734375, -0.13720703125 },
					{ -0.804931640625, -0.13818359375 }, { -0.83740234375, -0.14013671875 },
					{ -0.87109375, -0.142822265625 }, { -0.90625, -0.146484375 }, { -0.94287109375, -0.15087890625 },
					{ -0.973388671875, -0.156494140625 }, { -1.013427734375, -0.162353515625 },
					{ -1.05419921875, -0.168701171875 }, { -1.096435546875, -0.175537109375 },
					{ -1.1396484375, -0.182373046875 }, { -1.183837890625, -0.189208984375 },
					{ -1.228271484375, -0.195556640625 }, { -1.265625, -0.201416015625 },
					{ -1.310791015625, -0.20654296875 }, { -1.35595703125, -0.210693359375 },
					{ -1.401123046875, -0.2138671875 }, { -1.446044921875, -0.216064453125 },
					{ -1.489013671875, -0.217041015625 }, { -1.5322265625, -0.217041015625 },
					{ -1.575439453125, -0.2158203125 }, { -1.60888671875, -0.2138671875 },
					{ -1.65234375, -0.211669921875 }, { -1.69580078125, -0.209716796875 },
					{ -1.738525390625, -0.2080078125 }, { -1.782470703125, -0.206787109375 },
					{ -1.82763671875, -0.206787109375 }, { -1.87158203125, -0.207763671875 },
					{ -1.907958984375, -0.208984375 }, { -1.952392578125, -0.210693359375 },
					{ -1.9970703125, -0.212158203125 }, { -2.041015625, -0.21337890625 },
					{ -2.085205078125, -0.2138671875 }, { -2.129638671875, -0.2138671875 },
					{ -2.1640625, -0.213623046875 }, { -2.208251953125, -0.213134765625 },
					{ -2.254638671875, -0.212646484375 }, { -2.29833984375, -0.21240234375 },
					{ -2.343017578125, -0.212646484375 }, { -2.38720703125, -0.213134765625 },
					{ -2.4306640625, -0.21337890625 }, { -2.4736328125, -0.213623046875 },
					{ -2.507568359375, -0.213134765625 }, { -2.549560546875, -0.2119140625 },
					{ -2.590576171875, -0.2099609375 }, { -2.631591796875, -0.20556640625 },
					{ -2.672119140625, -0.20556640625 }, { -2.711669921875, -0.203125 },
					{ -2.750732421875, -0.200439453125 }, { -2.78173828125, -0.19775390625 },
					{ -2.81982421875, -0.195068359375 }, { -2.856689453125, -0.1923828125 },
					{ -2.892578125, -0.189453125 }, { -2.96142578125, -0.183349609375 }, { -2.99365234375, -0.1796875 },
					{ -3.018310546875, -0.175537109375 }, { -3.048095703125, -0.1708984375 },
					{ -3.075927734375, -0.16552734375 }, { -3.10302734375, -0.1591796875 },
					{ -3.12841796875, -0.152587890625 }, { -3.153076171875, -0.145751953125 },
					{ -3.177734375, -0.13916015625 }, { -3.19677734375, -0.133056640625 },
					{ -3.220458984375, -0.1279296875 }, { -3.242919921875, -0.12353515625 },
					{ -3.26513671875, -0.119873046875 }, { -3.287353515625, -0.11669921875 },
					{ -3.308349609375, -0.114013671875 }, { -3.329345703125, -0.111328125 },
					{ -3.345703125, -0.10888671875 }, { -3.36572265625, -0.1064453125 },
					{ -3.38525390625, -0.10400390625 }, { -3.403564453125, -0.101806640625 },
					{ -3.438232421875, -0.096923828125 }, { -3.438232421875, -0.096923828125 },
					{ -3.454345703125, -0.093994140625 }, { -3.46630859375, -0.0908203125 },
					{ -3.48046875, -0.087158203125 }, { -3.49365234375, -0.083251953125 },
					{ -3.506591796875, -0.078857421875 }, { -3.51806640625, -0.07470703125 },
					{ -3.52880859375, -0.070556640625 }, { -3.53955078125, -0.06640625 }, { -3.54736328125, -0.0625 },
					{ -3.556396484375, -0.05859375 }, { -3.56494140625, -0.05517578125 },
					{ -3.5732421875, -0.0517578125 }, { -3.58056640625, -0.048583984375 },
					{ -3.586669921875, -0.045654296875 }, { -3.592041015625, -0.04248046875 },
					{ -3.595458984375, -0.039306640625 }, { -3.5986328125, -0.03173828125 },
					{ -3.60009765625, -0.03173828125 }, { -3.600341796875, -0.027099609375 },
					{ -3.599609375, -0.021728515625 }, { -3.59912109375, -0.01611328125 },
					{ -3.5986328125, -0.010498046875 }, { -3.598388671875, -0.00146484375 },
					{ -3.598388671875, -0.00146484375 }, { -3.598388671875, 0.00048828125 },
					{ -3.598388671875, 0.00146484375 }, { -3.598388671875, 0.00146484375 }, };
			return mpR;
		};

		@Override
		public Integer mpDur() {
			return 20;
		};
	};

}